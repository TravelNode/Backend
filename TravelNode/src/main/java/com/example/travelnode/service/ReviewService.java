package com.example.travelnode.service;

import com.example.travelnode.S3.S3Uploader;
import com.example.travelnode.dto.ReviewRequestDto;
import com.example.travelnode.entity.Comment;
import com.example.travelnode.entity.Image;
import com.example.travelnode.entity.Review;
import com.example.travelnode.entity.User;
import com.example.travelnode.repository.CommentRepository;
import com.example.travelnode.repository.ImageRepository;
import com.example.travelnode.repository.ReviewRepository;
import com.example.travelnode.repository.RoutePlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RoutePlaceRepository routePlaceRepository;
    private final CommentRepository commentRepository;
    private final ImageRepository imgRepository;
    private final S3Uploader uploader;

    @Transactional
    public Review reviewDetails(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(() ->
                new IllegalArgumentException(("해당 리뷰가 존재하지 않습니다.")));
    }

    @Transactional
    public Review registerReview(ReviewRequestDto reviewDto, List<MultipartFile> reviewImgs,
                                 Long placeId, User user) throws IOException {

        // 한줄 소감, 리뷰 본문, 사진 데이터가 모두 없는 경우 다른 로직을 수행하지 않고 null 리턴
        if(reviewDto.getCommentId() == null && reviewDto.getReviewText() == null &&
                reviewImgs.get(0).isEmpty()) { return null; }

        // 한줄 소감을 입력하지 않은 경우 (리뷰 본문을 입력하지 않은 경우는 null으로 저장 가능)
        Review review;
        if(reviewDto.getCommentId() == null) {
            review = Review.builder().user(user).routePlace(routePlaceRepository.findById(placeId).orElseThrow()).
                     reviewText(reviewDto.getReviewText()).build();
        }
        else {
            review = Review.builder().user(user).routePlace(routePlaceRepository.findById(placeId).orElseThrow()).
                     comment(commentRepository.findById(reviewDto.getCommentId()).orElseThrow()).
                     reviewText(reviewDto.getReviewText()).build();
        }

        // 이미지를 제외한 리뷰 먼저 저장
        reviewRepository.save(review);

        // 리뷰 본문 or 한줄 소감만 입력하고 사진을 입력하지 않는 경우 image 관련 작업을 수행하지 않고 바로 return
        if(reviewImgs.get(0).isEmpty()) { return review; }

        // 리뷰 객체에 이미지 업데이트
        List<Image> images = new ArrayList<>();
        for (MultipartFile reviewImg : reviewImgs) {
            System.out.println(reviewImg);
            String filename = uploader.upload(reviewImg, "reviews");
            String imgName = reviewImg.getOriginalFilename();
            String imgKey = reviewImg.getContentType();
            Image image = Image.builder().review(review).imgName(imgName).imgKey(imgKey).imgUrl(filename).build();
            images.add(image);
        }

        List<Image> imageList = imgRepository.saveAll(images);
        review.saveImage(imageList);

        return review;
    }

    @Transactional
    public Comment changeComment(Long reviewId, Long commentId){
        Review review = reviewRepository.findById(reviewId).orElseThrow(() ->
                new IllegalArgumentException(("해당 리뷰가 존재하지 않습니다.")));
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        review.changeComment(comment);

        return review.getComment();
    }

    @Transactional
    public String changeReviewText(Long reviewId, String reviewText) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() ->
                new IllegalArgumentException(("해당 리뷰가 존재하지 않습니다.")));

        review.changeReviewText(reviewText);
        return review.getReviewText();
    }

    // 이미지 수정 method 필요

    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new IllegalArgumentException(("해당 리뷰가 존재하지 않습니다.")));

        reviewRepository.delete(review);
    }
}
