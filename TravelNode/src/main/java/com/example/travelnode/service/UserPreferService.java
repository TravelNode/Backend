package com.example.travelnode.service;

import com.example.travelnode.dto.UserPreferDto;
import com.example.travelnode.entity.PreferenceList;
import com.example.travelnode.entity.User;
import com.example.travelnode.entity.UserPreference;
import com.example.travelnode.repository.PreferenceListRepository;
import com.example.travelnode.repository.UserPreferRepository;
import com.example.travelnode.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserPreferService {

    private final PreferenceListRepository listRepository;
    private final UserPreferRepository upRepository;
    private UserRepository userRepository;

    public void saveList(PreferenceList pl){
        listRepository.save(pl);
    }

    public void saveUP(UserPreferDto dto){
        upRepository.save(dto.toEntity());
    }

    public List<PreferenceList> findAllPrefers(){
        return listRepository.findAll();
    }

    // PreferenceList DB에서 prefer_id로 description 찾기
    public String findDescription(Long prefer_id){
        PreferenceList pl = listRepository.findById(prefer_id).get();
        return pl.getDescription();
    }

    // PreferenceList DB에서 prefer_id로 question_id 찾기
    public Integer findQuestionID(Long prefer_id){
        PreferenceList pl = listRepository.findById(prefer_id).get();
        return pl.getQuestion_id();
    }

    // User 정보 받아서 -> User의 Prefer정보 get 하기
    public List<UserPreference> findAllList(Long uid){
        User user = userRepository.getReferenceById(uid);
        user.getPrefer_list();

        return user.getPrefer_list();
    }

}
