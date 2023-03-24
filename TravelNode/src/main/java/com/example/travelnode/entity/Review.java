package com.example.travelnode.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "REVIEW")
public class Review {

    @Id
    @Column(name = "REVIEW_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "USER_ID")
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
    @OneToMany(mappedBy = "review", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Image> reviewImages;

    @Builder
    public Review(User user, RoutePlace routePlace, Comment comment, String reviewText){
        Assert.hasText(String.valueOf(user), "User must not be empty");
        Assert.hasText(String.valueOf(routePlace), "RoutePlace must not be empty");

        this.user = user;
        this.routePlace = routePlace;
        this.comment = comment;
        this.reviewText = reviewText;
    }

    public void saveImage(List<Image> reviewImages) {
        this.reviewImages = reviewImages;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void changeComment(Comment comment) {
        this.comment = comment;
    }

    public void changeReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
}
