package com.example.travelnode.dto;

import com.example.travelnode.entity.Route;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class RouteResponseDto {

    private final Long id;
    private final Long cityId;
    private final Long keyword1;
    private final Long keyword2;
    private final String routeName;
    private final LocalDate routeDay;
    private final boolean open;

    public RouteResponseDto(Route entity){
        this.id = entity.getRouteId();
        this.cityId = entity.getCity().getId();
        this.keyword1 = entity.getKeyword1().getKeyId();
        this.keyword2 = entity.getKeyword2().getKeyId();
        this.routeName = entity.getRouteName();
        this.routeDay = entity.getRouteDay();
        this.open = entity.getIsPrivate();
    }


}
