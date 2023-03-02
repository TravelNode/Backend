package com.example.travelnode.dto;

import com.example.travelnode.entity.Comment;
import com.example.travelnode.entity.Image;
import com.example.travelnode.entity.Review;
import com.example.travelnode.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {

    private User user;
    private Comment comment;
    private String reviewText;

    public Review toEntity(){
        return Review.builder().
                user(user).
                comment(comment).
                reviewText(reviewText).build();
    }

}
