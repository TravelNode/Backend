package com.example.travelnode.controller;

import com.example.travelnode.entity.RoleType;
import com.example.travelnode.entity.User;
import com.example.travelnode.oauth2.entity.UserPrincipal;
import com.example.travelnode.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/oauth2/login-info")
    public UserPrincipal oauth2Login(@AuthenticationPrincipal UserPrincipal userPrincipal) throws Exception {

        if(userPrincipal == null){
            throw new Exception("Empty Principal");
        }

        return userPrincipal;
    }

    @PatchMapping("/change-admin")
    public User changeRoleToAdmin(@AuthenticationPrincipal UserPrincipal userPrincipal) throws Exception {

        if(userPrincipal == null){
            throw new Exception("Empty Principal");
        }

        return userService.changeRole(userPrincipal, RoleType.ADMIN);
    }

    @PatchMapping("/change-user")
    public User changeRoleToUser(@AuthenticationPrincipal UserPrincipal userPrincipal) throws Exception {

        if(userPrincipal == null){
            throw new Exception("Empty Principal");
        }

        return userService.changeRole(userPrincipal, RoleType.USER);
    }
}
