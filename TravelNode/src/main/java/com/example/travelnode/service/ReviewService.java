package com.example.travelnode.service;

import com.example.travelnode.S3.S3Uploader;
import com.example.travelnode.dto.ReviewRequestDto;
import com.example.travelnode.entity.Comment;
import com.example.travelnode.entity.Image;
import com.example.travelnode.entity.Review;
import com.example.travelnode.repository.CommentRepository;
import com.example.travelnode.repository.ImageRepository;
import com.example.travelnode.repository.ReviewRepository;
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

    private final ReviewRepository repository;
    private final CommentRepository commentRepository;
    private final ImageRepository imgRepository;
    private final S3Uploader uploader;

    /**
    @Transactional
    public Review registerReview(ReviewRequestDto reviewDto, Long placeId) throws IOException {
        List<Image> reviewImgs = new ArrayList<>();

        for(int i=0; i<reviewDto.getImages().size(); i++) {
            String filename = uploader.upload(reviewDto.getImages().get(i), "reviews");
            String imgName = reviewDto.getImages().get(i).getOriginalFilename();
            String imgKey = reviewDto.getImages().get(i).getContentType();
            String imgUrl = filename;

            Image image = Image.builder().review().build();
        }

        imgRepository.save(image1); // 이미지 먼저 저장
        Review review = dto.toEntity();
        review.saveImage(image1);
        repository.save(review); // 리뷰 저장

        return review;
    }

    @Transactional
    public Comment findComment(Long comment_id){
        return commentRepository.getReferenceById(comment_id);
    }
    **/
}
