package com.example.travelnode.oauth2.controller;

import com.example.travelnode.oauth2.properties.AppProperties;
import com.example.travelnode.oauth2.provider.JwtTokenProvider;
import com.example.travelnode.oauth2.repository.TokenRepository;
import com.example.travelnode.oauth2.response.ApiResponse;
import com.example.travelnode.oauth2.response.ApiResponseHeader;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController<T> {

    // private final AppProperties appProperties;
    private final JwtTokenProvider jwtTokenProvider;
    // private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    private final static long THREE_DAYS_MSEC = 259200000;
    private final static String REFRESH_TOKEN = "refresh_token";

    /**
    public ApiResponse<T> login(HttpServletRequest request, HttpServletResponse response) {
        return new ApiResponse<T>(new ApiResponseHeader(1));
    }
    **/
}
