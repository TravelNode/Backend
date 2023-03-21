
package com.example.travelnode.entity;

import com.example.travelnode.dto.RouteDayUpdateRequestDto;
import com.example.travelnode.dto.RouteNameUpdateRequestDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @Column(name = "ROUTE_DAY")
    private LocalDate routeDay;

    //@ColumnDefault("false")
    @Column(name = "IS_PRIVATE")
    private Boolean isPrivate;

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
    public Route(City city, KeywordList keyword1, KeywordList keyword2, String routeName,
                 LocalDate routeDay, boolean isPrivate ) {
        // this.user = user;
        this.city = city;
        this.keyword1 = keyword1;
        this.keyword2 = keyword2;
        this.routeName = routeName;
        this.routeDay = routeDay;
        this.isPrivate = isPrivate;
    }

    // 도시, 키워드 수정 부분 --> 엔티티라 this.city = dto.getCityId(); 가 안됨...
    public void updateCity(City city){
        this.city = city;
    }

    public void updateRouteName(RouteNameUpdateRequestDto dto) {
        this.routeName = dto.getRouteName();
    }

    public void updateRouteDay(RouteDayUpdateRequestDto dto) {
        this.routeDay = dto.getRouteDay();
    }

    public void updateKeyword1(KeywordList keyword) {
        this.keyword1 = keyword;
    }

    public void updateKeyword2(KeywordList keyword) {
        this.keyword2 = keyword;
    }

    public void updateRoutePrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }
}