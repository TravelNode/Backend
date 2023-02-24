package com.example.travelnode.controller;

import com.example.travelnode.dto.UserPreferDto;
import com.example.travelnode.entity.PreferenceList;
import com.example.travelnode.entity.User;
import com.example.travelnode.repository.PreferenceListRepository;
import com.example.travelnode.service.UserPreferService;
import com.example.travelnode.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/prefer")
@RequiredArgsConstructor
public class UserPreferController {
    @Autowired
    private UserPreferService upService;
    @Autowired
    private UserService userService;

    // PreferenceList DB에 post(사용자가 직접 쓸 일 x)
    @PostMapping("/addPreferList")
    public String addPreferenceList(@RequestBody PreferenceList pl){
        upService.saveList(pl);
        return pl.getDescription();
    }

    @GetMapping("/getdes")
    public String showList(@RequestParam("prefer_id") Long prefer_id){
        return upService.findDescription(prefer_id);
    }

    // 질문별로 취향 저장하기
    @PostMapping("/submit/{question_id}")
    public String addPrefer(@RequestParam("prefer_id") Long prefer_id,
                            @RequestParam("uid") Long uid,
                            @PathVariable("question_id") int question_id){
        // user 정보 받아오기
        User user = userService.findUser(uid);
        UserPreferDto dto = new UserPreferDto();

        // preference_list에서 prefer_id로 검색
        dto.setDescription(upService.findDescription(prefer_id));
        dto.setQuestion_id(question_id);
        dto.setUser(user);
        dto.setPrefer_id(prefer_id);

        // 여행성향 저장하기
        upService.saveUP(dto);

        // 유저-여행성향 업데이트하기
        user.addPrefer(dto.toEntity());
        return "updated";
    }

}
