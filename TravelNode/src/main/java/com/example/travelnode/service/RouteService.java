package com.example.travelnode.service;

import com.example.travelnode.dto.*;
import com.example.travelnode.entity.City;
import com.example.travelnode.entity.KeywordList;
import com.example.travelnode.entity.Route;
import com.example.travelnode.entity.User;
import com.example.travelnode.oauth2.entity.UserPrincipal;
import com.example.travelnode.repository.CityRepository;
import com.example.travelnode.repository.KeywordRepository;
import com.example.travelnode.repository.RouteRepository;
import com.example.travelnode.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final RouteRepository routeRepository;
    private final KeywordRepository keywordRepository;


    @Transactional // 루트별 조회
    public RouteResponseDto searchByRouteId(Long routeId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new IllegalArgumentException(("해당 루트가 존재하지 않습니다.")));
        return new RouteResponseDto(route);
    }

    @Transactional // 루트 정보 저장
    public Route createRoute(RouteCreateRequestDto requestDto, UserPrincipal userPrincipal) {
        System.out.println(requestDto.getRouteName() + " " + requestDto.getCityId());
        System.out.println(requestDto.getCityId().getClass());
        User user = userRepository.findByUniqueId(userPrincipal.getUniqueId());
        City city = cityRepository.findById(requestDto.getCityId()).orElseThrow();
        KeywordList keyword1 = keywordRepository.findById(requestDto.getKeyId1()).orElseThrow();
        KeywordList keyword2 = keywordRepository.findById(requestDto.getKeyId2()).orElseThrow();

        Route route = Route.builder().user(user).city(city).keyword1(keyword1).keyword2(keyword2).
                      routeName(requestDto.getRouteName()).isPrivate(requestDto.getIsPrivate()).
                      routeDay(requestDto.getRouteDay()).scrapCount(0).build();

        return routeRepository.save(route);
    }

    @Transactional // 여행 도시 수정
    public City updateCity(Long routeId, Long cityId) {
        Route route = routeRepository.findById(routeId).orElseThrow(
                      () -> new IllegalArgumentException(("해당 루트가 존재하지 않습니다.")));

        City city = cityRepository.findById(cityId).orElseThrow();
        route.updateCity(city);

        return route.getCity();
    }

    @Transactional // 루트 키워드 수정
    public KeywordList updateKeyword(Long routeId, Long currentKey, Long newKey) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 루트가 존재하지 않습니다."));

        KeywordList keyword = keywordRepository.findById(newKey).orElseThrow();
        if(route.getKeyword1().getKeyId().equals(currentKey))
            route.updateKeyword1(keyword);
        else
            route.updateKeyword2(keyword);

        return keyword;
    }

    @Transactional
    public String updateRouteName(Long routeId, String routeName) {
        Route route = routeRepository.findById(routeId).orElseThrow(
                () -> new IllegalArgumentException(("해당 루트가 존재하지 않습니다.")));

        route.updateRouteName(routeName);
        return route.getRouteName();
    }

    @Transactional
    public LocalDate updateRouteDay(Long routeId, LocalDate routeDay) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new IllegalArgumentException(("해당 루트가 존재하지 않습니다.")));

        route.updateRouteDay(routeDay);
        return route.getRouteDay();
    }

    @Transactional // 루트 공개 여부
    public Boolean updateRoutePrivate(Long routeId, Boolean isPrivate) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new IllegalArgumentException(("해당 루트가 존재하지 않습니다.")));

        route.updateRoutePrivate(isPrivate);
        return route.getIsPrivate();
    }

    @Transactional
    public void deleteRoute(Long routeId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new IllegalArgumentException(("해당 루트가  존재하지 않습니다.")));

        routeRepository.delete(route);
    }
}


