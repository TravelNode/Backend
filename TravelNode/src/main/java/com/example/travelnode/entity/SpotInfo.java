package com.example.travelnode.entity;

import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
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

    @Column(name = "LATITUDE")
    private Double latitude; // 위도

    @Column(name = "LONGITUDE")
    private Double longitude; // 경도
}
