package com.example.travelnode.controller;

import com.example.travelnode.entity.Comment;
import com.example.travelnode.entity.Review;
import com.example.travelnode.service.ReviewService;
import com.example.travelnode.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/users/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;

    @GetMapping("/{reviewId}")
    public Review reviewDetails(@PathVariable Long reviewId) {

        return reviewService.reviewDetails(reviewId);
    }

    @PutMapping("/update/comment/{reviewId}")
    public Comment updateComment(@PathVariable Long reviewId, @RequestBody Long commentId) {

        return reviewService.changeComment(reviewId, commentId);
    }

    @PutMapping("/update/review-text/{reviewId}")
    public String updateReviewText(@PathVariable Long reviewId, @RequestBody String reviewText) {

        return reviewService.changeReviewText(reviewId, reviewText);
    }

    @DeleteMapping("/delete/{reviewId}")
    public void deleteReview(@PathVariable Long reviewId) {

        reviewService.deleteReview(reviewId);
    }
}
