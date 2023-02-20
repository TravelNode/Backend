package com.example.travelnode.entity;

import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "ROUTE_PLACE")
public class RoutePlace {

    @Id
    @Column(name = "PLACE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long placeId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "SPOT_NAME", foreignKey = @ForeignKey(name = "fk_place_spot"))
    private SpotInfo spot;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "ROUTE_ID", foreignKey = @ForeignKey(name = "fk_place_route"))
    private Route route;

    @NotNull
    @Column(name = "PRIORITY")
    private Integer priority;

    @Column(name = "VISIT_TIME")
    private LocalDateTime visitTime;
}
