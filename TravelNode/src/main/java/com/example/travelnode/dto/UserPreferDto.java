package com.example.travelnode.dto;

import com.example.travelnode.entity.User;
import com.example.travelnode.entity.UserPreference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPreferDto {

    private String description;

    private int question_id;
    private Long prefer_id;
    private User user;


    public UserPreference toEntity() {
        return UserPreference.builder().
                prefer_id(prefer_id).
                description(description).
                question_id(question_id).
                user(user).
                build();
    }


}