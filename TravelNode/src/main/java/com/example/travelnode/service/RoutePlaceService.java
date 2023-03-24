package com.example.travelnode.service;

import com.example.travelnode.dto.PlaceRegisterRequestDto;
import com.example.travelnode.dto.SpotInfoDto;
import com.example.travelnode.entity.Route;
import com.example.travelnode.entity.RoutePlace;
import com.example.travelnode.entity.SpotInfo;
import com.example.travelnode.entity.User;
import com.example.travelnode.repository.RoutePlaceRepository;
import com.example.travelnode.repository.RouteRepository;
import com.example.travelnode.repository.SpotInfoRepository;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RoutePlaceService {

    @Value("${app.kakao.rest-api-key}")
    private String kakaoApiKey;
    private final SpotInfoRepository spotInfoRepository;
    private final RouteRepository routeRepository;
    private final RoutePlaceRepository routePlaceRepository;

    // Kakao Map에서 받은 여러 개의 데이터를 현재 위치에서 가까운 순으로 정렬해서 리턴 or DB에 저장된 장소 정보 리턴
    public List<SpotInfoDto> locationInfoList(String loc, Double longitude, Double latitude) throws ParseException {
        List<SpotInfoDto> placeInfoList = new ArrayList<>();
        SpotInfo spot = spotInfoRepository.findSpotInfoBySpotName(loc.replaceAll(" ", ""));

        if(spot != null) { // 해당 장소가 이미 DB에 저장되어 있는지 확인하고, 저장된 장소라면 값을 불러옴
            placeInfoList.add(SpotInfoDto.builder().spotName(spot.getSpotName()).address(spot.getAddress())
                    .longitude(spot.getLongitude()).latitude(spot.getLatitude()).build());

            return placeInfoList;
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "KakaoAK " + kakaoApiKey);

        String kakaoMapUrl = "https://dapi.kakao.com/v2/local/search/keyword.json";
        String requestUrl = UriComponentsBuilder.fromHttpUrl(kakaoMapUrl)
                .queryParam("query", loc).build().toUriString();

        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> placeInfos = restTemplate.exchange(requestUrl, HttpMethod.GET, entity, String.class);

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(placeInfos.getBody());
        JSONArray jsonArray = (JSONArray) jsonObject.get("documents");

        if(jsonArray.size() == 0) { // API에서 검색되는 장소 정보가 없는 경우
            // 단순히 사용자가 입력한 장소 이름, 현재 좌표만 저장, 주소는 empty string
            placeInfoList.add(new SpotInfoDto(loc, "", longitude, latitude));
            return placeInfoList;
        }

        List<JSONObject> placeLists = new ArrayList<>(); // 현재 좌표를 기준으로 검색된 장소들을 가까운 거리 순으로 정렬
        for (Object o : jsonArray) {
            placeLists.add((JSONObject) o);
        }

        placeLists.sort((a, b) -> {
            double aLong = Double.parseDouble((String) a.get("x"));
            double aLat = Double.parseDouble((String) a.get("y"));
            double bLong = Double.parseDouble((String) b.get("x"));
            double bLat = Double.parseDouble((String) b.get("y"));

            double longDiffA = Math.toRadians(longitude - aLong);
            double latDiffA = Math.toRadians(latitude - aLat);
            double longDiffB = Math.toRadians(longitude - bLong);
            double latDiffB = Math.toRadians(latitude - bLat);

            double sinA = Math.sin(latDiffA / 2) * Math.sin(latDiffA / 2)
                    + Math.cos(Math.toRadians(aLat)) * Math.cos(Math.toRadians(latitude))
                    * Math.sin(longDiffA / 2) * Math.sin(longDiffA / 2);
            double sinB = Math.sin(latDiffB / 2) * Math.sin(latDiffB / 2)
                    + Math.cos(Math.toRadians(bLat)) * Math.cos(Math.toRadians(latitude))
                    * Math.sin(longDiffB / 2) * Math.sin(longDiffB / 2);

            double tanA = 2 * Math.atan2(Math.sqrt(sinA), Math.sqrt(1 - sinA));
            double tanB = 2 * Math.atan2(Math.sqrt(sinB), Math.sqrt(1 - sinB));

            // 지구 반지름을 6371km로 가정하고 거리 계산
            double distanceA = 6371 * tanA * 1000;
            double distanceB = 6371 * tanB * 1000;

            return Double.compare(distanceA, distanceB);
        });

        for (JSONObject object : placeLists) {
            if (object.get("road_address_name").equals("")) {
                placeInfoList.add(new SpotInfoDto((String) object.get("place_name"), (String) object.get("address_name"),
                        Double.parseDouble((String) object.get("x")), Double.parseDouble((String) object.get("y"))));
            }
            else {
                placeInfoList.add(new SpotInfoDto((String) object.get("place_name"), (String) object.get("road_address_name"),
                        Double.parseDouble((String) object.get("x")), Double.parseDouble((String) object.get("y"))));
            }
        }
        return placeInfoList;
    }

    @Transactional
    public RoutePlace registerRoutePlace(PlaceRegisterRequestDto placeRegisterRequestDto,
                                         Route route, User user) throws Exception {
        if(route == null) {
            throw new NullPointerException("존재하지 않는 루트입니다.");
        }

        String spotName = placeRegisterRequestDto.getSpotName().replaceAll(" ", "").trim();
        SpotInfo spot = spotInfoRepository.findSpotInfoBySpotName(spotName);
        if(spot == null) { // 이전에 등록된 적 없는 장소인 경우 Kakao Map API로 장소 정보 검색 -> SpotInfo 테이블에 정보 저장
            SpotInfoDto spotInfoDto = locationInfoList(spotName, placeRegisterRequestDto.getLongitude(),
                    placeRegisterRequestDto.getLatitude()).get(0);

            if(spotInfoDto.getSpotName().replaceAll(" ", "").equals(spotName)) {
                spotName = spotInfoDto.getSpotName();
            }
            spot = spotInfoRepository.save(spotInfoDto.toEntity());
        }

        RoutePlace routePlace = RoutePlace.builder().user(user).spot(spot).placeName(spotName).route(route)
                .priority(placeRegisterRequestDto.getPriority()).visitTime(placeRegisterRequestDto.getVisitTime()).build();

        return routePlaceRepository.save(routePlace);
    }

    @Transactional
    public RoutePlace updatePlaceName(String prevName, String placeName) {
        RoutePlace routePlace = routePlaceRepository.findBySpotName(prevName)
                .orElseThrow(() -> new IllegalArgumentException("해당 장소가 루트에 존재하지 않습니다."));

        routePlace.update(placeName);
        return routePlace;
    }

    @Transactional
    public void deletePlace(Long placeId, String routeName, Integer priority) {
        Route route = routeRepository.findRouteByRouteName(routeName)
                .orElseThrow(() -> new IllegalArgumentException("해당 장소가 루트에 존재하지 않습니다."));

        routePlaceRepository.deleteById(placeId);

        // 장소가 삭제된 후, 같은 루트에 포함된 다른 장소들의 priority 재설정
        for(int i=priority+1; i<=5; i++) {
            RoutePlace routePlace = routePlaceRepository.findByRouteAndPriority(route, i)
                    .orElseThrow(() -> new IOException("루트에 포함된 마지막 장소입니다."));

            routePlace.changePriority(routePlace.getPriority()-1);
        }
    }

    @Transactional
    public List<RoutePlace> allPlacesInRoute(String routeName) {

        Route route = routeRepository.findRouteByRouteName(routeName)
                .orElseThrow(() -> new NullPointerException("존재하지 않는 루트입니다."));

        return routePlaceRepository.findAllPlacesByRoute(route);
    }
}
