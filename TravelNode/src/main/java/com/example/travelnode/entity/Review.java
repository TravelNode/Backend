package com.example.travelnode.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "REVIEW")
public class Review {

    @Id
    @Column(name = "REVIEW_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "user_uid")
    private User user;

//    @OneToOne
//    @JoinColumn(name = "PLACE_ID") // foreignKey = @ForeignKey(name = "fk_review_place")
//    private RoutePlace routePlace;

    @ManyToOne
    @JoinColumn(name = "COMMENT_ID") // foreignKey = @ForeignKey(name = "fk_review_comment")
    private Comment comment;

    @Column(name = "REVIEW_TEXT")
    private String reviewText;

    @ManyToOne
    @JoinColumn(name = "img_id")
    @JsonIgnore
    // @OneToOne(mappedBy = "review", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private Image image;

    @Builder // 사용자가 선호하는 여행 스타일을 입력 or 수정 or 삭제할 때
    public Review(User user,Comment comment, String reviewText, Image image){
        this.user = user; // user_id
        this.comment = comment; // comment_id
        this.reviewText = reviewText;
        this.image = image; // img_id
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
