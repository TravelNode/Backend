package com.example.travelnode.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

@Getter
public class ReviewRequestDto implements Serializable {

    private final Long commentId;
    private final String reviewText;

    @Builder
    public ReviewRequestDto(Long commentId, String reviewText, List<MultipartFile> images) {
        this.commentId = commentId;
        this.reviewText = reviewText;
    }
}
