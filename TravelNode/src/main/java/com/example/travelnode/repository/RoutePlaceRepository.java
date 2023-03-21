package com.example.travelnode.repository;

import com.example.travelnode.entity.Route;
import com.example.travelnode.entity.RoutePlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoutePlaceRepository extends JpaRepository<RoutePlace, Long>, JpaSpecificationExecutor<RoutePlace> {

    @Query("SELECT rp FROM RoutePlace rp WHERE rp.spot.spotName = ?1")
    Optional<RoutePlace> findBySpotName(String placeName);

    Optional<RoutePlace> findByRouteAndPriority(Route route, Integer priority);

    @Query("SELECT rp FROM RoutePlace rp WHERE rp.route = ?1 ORDER BY rp.priority")
    List<RoutePlace> findAllPlacesByRoute(Route route);
}
