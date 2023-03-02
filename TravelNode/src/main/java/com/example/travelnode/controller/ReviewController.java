package com.example.travelnode.controller;

import com.example.travelnode.dto.ReviewDto;
import com.example.travelnode.entity.Comment;
import com.example.travelnode.entity.User;
import com.example.travelnode.service.ReviewService;
import com.example.travelnode.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/users/review")
@RequiredArgsConstructor
public class ReviewController {

    @Autowired
    private ReviewService service;
    @Autowired
    private UserService userService;


    @PostMapping("/write")
    public String addReview(HttpServletRequest request,
                            @RequestParam("uid") Long uid,
                            @RequestParam("comment_id") Long comment_id,
                            @RequestParam("review_text") String review_text,
                            @RequestParam(value = "image") MultipartFile image) throws IOException{

        ReviewDto dto = new ReviewDto();
        User user = userService.findUser(uid);
        Comment comment = service.findComment(comment_id);

        dto.setUser(user);
        dto.setComment(comment);
        dto.setReviewText(review_text);

        service.saveReview(dto, image);
        return "success";
    }

}
