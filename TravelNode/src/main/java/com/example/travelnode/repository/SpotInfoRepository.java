package com.example.travelnode.repository;

import com.example.travelnode.entity.SpotInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SpotInfoRepository extends JpaRepository<SpotInfo, Long> {

    @Query("SELECT sp FROM SpotInfo sp WHERE REPLACE(sp.spotName, ' ', '') = ?1")
    SpotInfo findSpotInfoBySpotName(String spotName);
}
