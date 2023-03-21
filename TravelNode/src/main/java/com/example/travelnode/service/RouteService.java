package com.example.travelnode.service;

import com.example.travelnode.dto.*;
import com.example.travelnode.entity.City;
import com.example.travelnode.entity.KeywordList;
import com.example.travelnode.entity.Route;
import com.example.travelnode.repository.CityRepository;
import com.example.travelnode.repository.KeywordRepository;
import com.example.travelnode.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final CityRepository cityRepository;
    private final RouteRepository routeRepository;
    private final KeywordRepository keywordRepository;


    @Transactional // 루트별 조회
    public RouteResponseDto searchByRouteId(Long id) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(("해당 루트가 존재하지 않습니다.")));
        return new RouteResponseDto(route);
    }
    @Transactional // 루트 정보 저장
    public Route createRoute(RouteCreateRequestDto requestDto) {
        City city = cityRepository.findById(requestDto.getCityId()).orElseThrow();
        KeywordList keyword1 = keywordRepository.findById(requestDto.getKeyId1()).orElseThrow();
        KeywordList keyword2 = keywordRepository.findById(requestDto.getKeyId2()).orElseThrow();

        Route route = Route.builder().city(city).keyword1(keyword1).keyword2(keyword2)
                .routeName(requestDto.getRouteName()).routeDay(requestDto.getRouteDay()).build();

        return routeRepository.save(route);
    }

    // 검토 필요
    @Transactional // 여행 도시 수정
    public Long updateCity(Long routeId, Long cityId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new IllegalArgumentException(("해당 루트가 존재하지 않습니다.")));

        City city = cityRepository.findById(cityId).orElseThrow();
        route.updateCity(city);
        // routeRepository.save(route);

        return routeId;
    }

    @Transactional // 루트 키워드 수정
    public Route updateKeyword(Long id, Long currentKey, Long newKey) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 루트가 존재하지 않습니다."));

        KeywordList keyword = keywordRepository.findById(newKey).orElseThrow();
        if(route.getKeyword1().getKeyId().equals(currentKey))
            route.updateKeyword1(keyword);
        else
            route.updateKeyword2(keyword);

        // routeRepository.save(route);
        return route;
    }

    @Transactional
    public Long updateRouteName(Long id, com.example.travelnode.dto.RouteNameUpdateRequestDto requestDto) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(("해당 Route 가  존재하지 않습니다.")));
        route.updateRouteName(requestDto);
        // Dirty Cheking 왜 안되는거지..
        routeRepository.save(route);
        return id;
    }

    @Transactional
    public Long updateRouteDay(Long id, RouteDayUpdateRequestDto requestDto) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(("해당 루트가 존재하지 않습니다.")));
        route.updateRouteDay(requestDto);
        // Dirty Cheking
        routeRepository.save(route);
        return id;
    }

    @Transactional
    public void deleteRoute(Long id) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(("해당 Route 가  존재하지 않습니다.")));
        routeRepository.delete(route);
    }

    @Transactional // 루트 공개 여부
    public Long updateRouteOpen(Long id, RouteOpenUpdateRequestDto requestDto) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(("해당 Route 가  존재하지 않습니다.")));

        route.updateRoutePrivate(requestDto.isOpen());
        routeRepository.save(route);
        return id;
    }
}


