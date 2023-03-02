package com.example.travelnode.service;

import com.example.travelnode.S3.S3Uploader;
import com.example.travelnode.dto.ReviewDto;
import com.example.travelnode.entity.Comment;
import com.example.travelnode.entity.Image;
import com.example.travelnode.entity.Review;
import com.example.travelnode.repository.CommentRepository;
import com.example.travelnode.repository.ImageRepository;
import com.example.travelnode.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository repository;
    private final CommentRepository coRepository;
    private final ImageRepository imgRepository;
    @Autowired
    private S3Uploader uploader;

    public void saveReview(ReviewDto dto, MultipartFile image) throws IOException {
        Image image1 = new Image();
        if(!image.isEmpty()){
            String filename = uploader.upload(image, "reviews");
            image1.setImgUrl(filename);
            image1.setImgName(image.getOriginalFilename());
            image1.setImgKey(image.getContentType());
        }
        imgRepository.save(image1); // 이미지 먼저 저장
        Review review = dto.toEntity();
        review.setImage(image1);
        repository.save(review); // 리뷰 저장
    }

    public Comment findComment(Long comment_id){
        return coRepository.getReferenceById(comment_id);
    }
}
