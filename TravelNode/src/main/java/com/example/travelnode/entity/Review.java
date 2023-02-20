package com.example.travelnode.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "REVIEW")
public class Review {

    @Id
    @Column(name = "REVIEW_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    private User user;

    @NotNull
    @OneToOne
    @JoinColumn(name = "PLACE_ID", foreignKey = @ForeignKey(name = "fk_review_place"))
    private RoutePlace routePlace;

    @ManyToOne
    @JoinColumn(name = "COMMENT_ID", foreignKey = @ForeignKey(name = "fk_review_comment"))
    private Comment comment;

    @Column(name = "REVIEW_TEXT")
    private String reviewText;

    @JsonIgnore
    @OneToOne(mappedBy = "review", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private Image image;
}
