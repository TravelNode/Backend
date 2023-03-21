package com.example.travelnode.controller;

import com.example.travelnode.dto.*;
import com.example.travelnode.entity.Review;
import com.example.travelnode.entity.Route;
import com.example.travelnode.entity.RoutePlace;
import com.example.travelnode.service.ReviewService;
import com.example.travelnode.service.RoutePlaceService;
import com.example.travelnode.service.RouteService;
import com.example.travelnode.service.UserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/route")
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;
    private final RoutePlaceService routePlaceService;
    private final ReviewService reviewService;
    private final UserService userService;

    @GetMapping("/{id}") // 루트 조회
    public RouteResponseDto searchRouteById(@PathVariable Long id) {

        return routeService.searchByRouteId(id);
    }

    // 루트 + 장소정보 + 각 장소에 대한 리뷰 모두 등록
    // consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Route registerRoute(@RequestPart(value = "infos") RouteCreateRequestDto requestDto,
                               @RequestPart(value = "images[]")List<MultipartFile> reviewImgs) throws Exception {

        Route route = routeService.createRoute(requestDto);
        for(int i=0; i<requestDto.getPlaces().size(); i++) {
            RoutePlace routePlace = routePlaceService.registerRoutePlace(requestDto.getPlaces().get(i), route);
            // Review review = reviewService.registerReview(requestDto.getReviews().get(i), routePlace.getPlaceId());
            log.trace(String.valueOf(routePlace));
            // log.trace(String.valueOf(review));
        }

        return route;
    }

    @PatchMapping("/update/{routeId}/city") // 루트도시 수정
    public Long updateCity(@PathVariable Long routeId, @RequestParam Long cityId) {

        return routeService.updateCity(routeId, cityId);
    }

    @PatchMapping("/update/{routeId}/keyword") // 루트키워드 수정
    public Route updateKeyword(@PathVariable Long routeId, @RequestParam Long currentKey, @RequestParam Long newKey) {

        return routeService.updateKeyword(routeId, currentKey, newKey);
    }

    @PatchMapping("/update/route-name/{id}") // 루트이름 수정
    public Long updateRouteName(@PathVariable Long id, @RequestBody RouteNameUpdateRequestDto requestDto) {

        return routeService.updateRouteName(id, requestDto);
    }

    @PatchMapping("/update/route-date/{id}") // 루트날짜 수정
    public Long updateDay(@PathVariable Long id, @RequestBody RouteDayUpdateRequestDto requestDto) {

        return routeService.updateRouteDay(id, requestDto);
    }

    @PatchMapping("/update/route-open/{id}") // 루트 공개여부 결정
    public Long updateOpen(@PathVariable Long id, @RequestBody RouteOpenUpdateRequestDto requestDto) {

        return routeService.updateRouteOpen(id, requestDto);
    }

    @DeleteMapping("delete/route/{id}") // 루트 전체 삭제
    public void deleteRoute(@PathVariable Long id){

        routeService.deleteRoute(id);
    }
}