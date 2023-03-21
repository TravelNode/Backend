package com.example.travelnode.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ROUTE_PLACE")
public class RoutePlace {

    @Id
    @Column(name = "PLACE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long placeId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "SPOT_ID", foreignKey = @ForeignKey(name = "fk_place_spot"))
    private SpotInfo spot;

    @NotNull
    @Column(name = "PLACE_NAME")
    private String placeName;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "ROUTE_ID", foreignKey = @ForeignKey(name = "fk_place_route"))
    private Route route;

    @NotNull
    @Column(name = "PRIORITY")
    private Integer priority;

    @Column(name = "VISIT_TIME")
    private LocalDateTime visitTime;

    @Builder
    public RoutePlace(@NotNull SpotInfo spot, @NotNull String placeName, @NotNull Route route, @NotNull Integer priority, LocalDateTime visitTime) {
        this.spot = spot;
        this.placeName = placeName;
        this.route = route;
        this.priority = priority;
        this.visitTime = visitTime != null ? visitTime : LocalDateTime.now();
    }

    public void update(String placeName) {
        this.placeName = placeName;
    }

    public void changePriority(Integer priority) {
        this.priority = priority;
    }
}
