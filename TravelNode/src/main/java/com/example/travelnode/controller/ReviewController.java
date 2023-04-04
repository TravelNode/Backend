package com.example.travelnode.controller;

import com.example.travelnode.dto.ImageChangeDto;
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

    @PatchMapping("/update/comment/{reviewId}")
    public Comment updateComment(@PathVariable Long reviewId, @RequestBody Long commentId) {

        return reviewService.changeComment(reviewId, commentId);
    }

    @PatchMapping("/update/review-text/{reviewId}")
    public String updateReviewText(@PathVariable Long reviewId, @RequestBody String reviewText) {

        return reviewService.changeReviewText(reviewId, reviewText);
    }

    @PatchMapping("/update/image/{reviewId}")
    public Long updateImage(@PathVariable Long reviewId, @RequestBody ImageChangeDto updateimg) {
        return reviewService.changeImage(reviewId, updateimg);
    }

    @DeleteMapping("/delete/{reviewId}")
    public void deleteReview(@PathVariable Long reviewId) {

        reviewService.deleteReview(reviewId);
    }
}
