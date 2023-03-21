package com.example.travelnode.repository;

import com.example.travelnode.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RouteRepository extends JpaRepository<Route, Long> {

    @Query("SELECT r FROM Route r WHERE r.routeName = ?1")
    Optional<Route> findRouteByRouteName(String routeName);
}
