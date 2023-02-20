package com.example.travelnode.entity;

import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Table(name = "CITY")
public class City {

    @Id
    @Column(name = "CITY_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cityId;

    @NotNull
    @Column(name = "CITY_NAME")
    private String cityName;
}
