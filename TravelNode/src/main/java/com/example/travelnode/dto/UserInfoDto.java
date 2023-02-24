package com.example.travelnode.dto;


import com.example.travelnode.entity.ProviderType;
import com.example.travelnode.entity.RoleType;
import com.example.travelnode.entity.User;
import com.example.travelnode.entity.UserPreference;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfoDto implements Serializable {
    private Long uid;
    private String email;
    private String nickname;
    private RoleType roleType;
    private ProviderType providerType;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    // private Avatar avatar;
    private Integer travelCount;
    private Integer level;

    //추가
    private List<UserPreference> prefer_list = new ArrayList<>();
    public void addPrefer(UserPreference up){
        this.prefer_list.add(up);
    }

    public UserInfoDto(User user) { // 인증(로그인) 된 사용자 정보
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        // this.avatar = user.getAvatar();
        this.travelCount = user.getTravelCount();
        this.level = user.getLevel();
    }
}
