package com.example.travelnode.controller;

import com.example.travelnode.dto.SpotInfoDto;
import com.example.travelnode.entity.RoutePlace;
import com.example.travelnode.service.RoutePlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/route/place")
@RequiredArgsConstructor
public class RoutePlaceController {

    private final RoutePlaceService routePlaceService;

    @GetMapping("/details")
    // 사용자가 입력한 장소 이름을 기준으로
    public List<SpotInfoDto> placeDetails(@RequestParam String loc, @RequestParam Double longitude,
                                 @RequestParam Double latitude) throws Exception {

        List<SpotInfoDto> spotInfoDto = routePlaceService.locationInfoList(loc, longitude, latitude);
        if(spotInfoDto.size() == 0)
            throw new Exception("Cannot Find Information");

        return spotInfoDto;
    }

    // 현재 루트에 포함된 모든 장소 리스트 리턴
    @GetMapping("/list")
    public List<RoutePlace> placeListInRoute(@RequestParam String routeName) {

        return routePlaceService.allPlacesInRoute(routeName);
    }

    /**
    @PostMapping("/register")
    // 장소 등록하기 버튼 클릭 시 현재 루트에 해당 장소 저장
    public RoutePlace registerPlace(@RequestBody PlaceRegisterRequestDto placeRegisterRequestDto) throws ParseException {

        return routePlaceService.registerRoutePlace(placeRegisterRequestDto);
    }
     **/

    @PatchMapping("/change-name")
    public RoutePlace changePlaceName(@RequestParam String prevName, @RequestParam String placeName) {

        return routePlaceService.updatePlaceName(prevName, placeName);
    }

    @DeleteMapping("/delete/{placeId}")
    public void deletePlace(@PathVariable(value = "placeId") Long placeId,
                            @RequestParam String routeName, @RequestParam Integer priority) {
        routePlaceService.deletePlace(placeId, routeName, priority);
    }
}
