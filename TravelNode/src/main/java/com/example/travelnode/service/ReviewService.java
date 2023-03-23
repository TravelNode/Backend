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
    public Review registerReview(ReviewRequestDto reviewDto, List<MultipartFile> reviewImgs,
                                 Long placeId, User user) throws IOException {

        // 이미지를 제외한 리뷰 먼저 저장
        Review review = Review.builder().user(user).routePlace(routePlaceRepository.findById(placeId).orElseThrow()).
                         comment(commentRepository.findById(reviewDto.getCommentId()).orElseThrow()).
                         reviewText(reviewDto.getReviewText()).build();
        reviewRepository.save(review);

        // 리뷰 객체에 이미지 업데이트
        List<Image> images = new ArrayList<>();
        for (MultipartFile reviewImg : reviewImgs) {
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
    public Comment findComment(Long comment_id){
        return commentRepository.getReferenceById(comment_id);
    }
}
