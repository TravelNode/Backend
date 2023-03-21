package com.example.travelnode.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "SPOT_INFO")
public class SpotInfo {

    @Id
    @Column(name = "SPOT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long spotId;

    @NotNull
    @Column(name = "SPOT_NAME")
    private String spotName;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "LONGITUDE")
    private Double longitude; // 경도

    @Column(name = "LATITUDE")
    private Double latitude; // 위도

    @Builder
    public SpotInfo(@NotNull String spotName, String address, Double longitude, Double latitude) {
        this.spotName = spotName;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public void update(String spotName) {
        this.spotName = spotName;
    }
}