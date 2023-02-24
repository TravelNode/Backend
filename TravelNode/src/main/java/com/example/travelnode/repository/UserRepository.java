package com.example.travelnode.repository;

import com.example.travelnode.dto.UserInfoDto;
import com.example.travelnode.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u.uid, u.email, u.nickname, u.roleType, u.providerType, u.createdAt, " +
            "u.modifiedAt, u.travelCount, u.level FROM User u")
    List<UserInfoDto> findAllByUid(Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.nickname = ?1")
    User findByNickname(String nickname);

    @Query("SELECT u FROM User u WHERE u.email = ?1")
    User findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.uniqueId = ?1")
    User findByUniqueId(String uniqueId);
}
