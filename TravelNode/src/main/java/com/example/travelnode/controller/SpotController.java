package com.example.travelnode.controller;

import com.example.travelnode.dto.SpotInfoDto;
import com.example.travelnode.entity.RoleType;
import com.example.travelnode.entity.SpotInfo;
import com.example.travelnode.entity.User;
import com.example.travelnode.oauth2.entity.UserPrincipal;
import com.example.travelnode.service.SpotService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/spot")
@RequiredArgsConstructor
public class SpotController {

    private final SpotService spotService;

    @GetMapping("/list")
    public List<SpotInfo> getSpotLists(@AuthenticationPrincipal UserPrincipal userPrincipal) throws Exception {

        if(userPrincipal == null) {
            throw new Exception("No User Information");
        }

        if(Objects.equals(userPrincipal.getRoleType().getCode(), RoleType.USER.getCode())) {
            throw new Exception("Do not have authority");
        }

        return spotService.getAllSpots();
    }

    @PostMapping("/register")
    public SpotInfo registerSpot(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                 @RequestBody SpotInfoDto spotInfoDto) throws Exception {

        if(userPrincipal == null) {
            throw new Exception("No User Information");
        }

        if(Objects.equals(userPrincipal.getRoleType().getCode(), RoleType.USER.getCode())) {
            throw new Exception("Do not have authority");
        }

        return spotService.registerSpotInfo(spotInfoDto);
    }

    @PatchMapping("/change-name")
    public SpotInfo updateSpotName(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                   @RequestParam String prevName, @RequestParam String spotName) throws Exception {

        if(userPrincipal == null) {
            throw new UserPrincipalNotFoundException("No User Information");
        }

        if(Objects.equals(userPrincipal.getRoleType().getCode(), RoleType.USER.getCode())) {
            throw new Exception("Do not have authority");
        }

        return spotService.updateSpotName(prevName, spotName);
    }

    @DeleteMapping("/delete/{spotName}")
    public void deleteSpot(@PathVariable(value = "spotName") String spotName,
                           @AuthenticationPrincipal UserPrincipal userPrincipal) throws Exception {

        if(userPrincipal == null) {
            throw new Exception("No User Information");
        }

        if(Objects.equals(userPrincipal.getRoleType().getCode(), RoleType.USER.getCode())) {
            throw new Exception("Do not have authority");
        }
        spotService.deleteSpotInfo(spotName);
    }
}
