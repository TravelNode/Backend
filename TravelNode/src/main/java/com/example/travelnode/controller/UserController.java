package com.example.travelnode.controller;

import com.example.travelnode.oauth2.entity.UserPrincipal;
import com.example.travelnode.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private UserRepository userRepository;

    @GetMapping("/oauth2/login-info")
    public UserPrincipal oauth2Login(@AuthenticationPrincipal UserPrincipal userPrincipal) throws Exception {

        if(userPrincipal == null){
            throw new Exception("Empty Principal");
        }

        return userPrincipal;
    }

    // 시작시 테스트용
    @GetMapping("/show")
    public String showStart(){
        return "Success";
    }
}
