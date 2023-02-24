package com.example.travelnode.service;

import com.example.travelnode.entity.User;
import com.example.travelnode.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    public User findUser(Long uid){
        return repository.getReferenceById(uid);
    }
}
