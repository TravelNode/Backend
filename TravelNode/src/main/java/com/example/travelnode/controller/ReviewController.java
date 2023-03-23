package com.example.travelnode.controller;

import com.example.travelnode.dto.ReviewRequestDto;
import com.example.travelnode.entity.Comment;
import com.example.travelnode.entity.User;
import com.example.travelnode.service.ReviewService;
import com.example.travelnode.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
}
