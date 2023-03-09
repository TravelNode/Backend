

package com.example.travelnode.entity;

import com.example.travelnode.dto.CityUpdateRequestDto;
import com.example.travelnode.dto.RouteDayUpdateRequestDto;
import com.example.travelnode.dto.RouteNameUpdateRequestDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@Table(name = "ROUTE")
public class Route {

    @Id
    @Column(name = "ROUTE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long routeId;

    //@NotNull
    @ManyToOne
    @JoinColumn(name = "UID", foreignKey = @ForeignKey(name = "fk_route_uid"))
    private User user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "CITY_ID", foreignKey = @ForeignKey(name = "fk_route_city"))
    private City city;

    //@NotNull
    @ManyToOne
    @JoinColumn(name = "KEY_ID1", foreignKey = @ForeignKey(name = "fk_route_keyword1"))
    private KeywordList keyword1;

    //@NotNull
    @ManyToOne
    @JoinColumn(name = "KEY_ID2", foreignKey = @ForeignKey(name = "fk_route_keyword2"))
    private KeywordList keyword2;

    //@NotNull
    @Size(max = 128)
    @Column(name = "ROUTE_NAME", length = 128)
    private String routeName;

    //@NotNull
    @ColumnDefault("0")
    @Column(name = "SCRAP_COUNT")
    private Integer scrapCount;

    //@NotNull
    @Column(name = "ROUTE_DAY")
    private LocalDate routeDay;

    @ColumnDefault("false")
    @Column(name = "IS_FOLLOWING")
    private Boolean isFollowing;

    @ColumnDefault("false")
    @Column(name = "IS_FINISHED")
    private Boolean isFinished;

    public void addScrap() {
        this.scrapCount++;
    }

    public void subScrap() {
        this.scrapCount--;
    }

    @Builder
    public Route(City city, KeywordList keyword1, KeywordList keyword2, String routeName, LocalDate routeDay ) {
        //this.user = user; 유저 정보 필요 없음
        this.city = city;
        this.keyword1 = keyword1;
        this.keyword2 = keyword2;
        this.routeName = routeName;
        this.routeDay = routeDay;

    }

    public void updateroutename(RouteNameUpdateRequestDto requestDto) {
        this.routeName = requestDto.getRouteName();
    }


    public void updaterouteday(RouteDayUpdateRequestDto requestDto) {
        this.routeDay = requestDto.getRouteDay();
    }
}
