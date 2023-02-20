package com.example.travelnode.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Date;

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

    @NotNull
    @ManyToOne
    @JoinColumn(name = "UID", foreignKey = @ForeignKey(name = "fk_route_uid"))
    private User user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "CITY_NAME", foreignKey = @ForeignKey(name = "fk_route_city"))
    private City city;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "KEYWORD", foreignKey = @ForeignKey(name = "fk_route_keyword1"))
    private KeywordList keyword1;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "KEYWORD", foreignKey = @ForeignKey(name = "fk_route_keyword2"))
    private KeywordList keyword2;

    @NotNull
    @Size(max = 128)
    @Column(name = "ROUTE_NAME", length = 128)
    private String routeName;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "SCRAP_COUNT")
    private Integer scrapCount;

    @NotNull
    @Column(name = "ROUTE_DAY")
    private Date routeDay;

    @ColumnDefault("false")
    @Column(name = "IS_FOLLOWING")
    private Boolean isFollowing;

    @ColumnDefault("false")
    @Column(name = "IS_FINISHED")
    private Boolean isFinished;
}
