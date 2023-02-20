package com.example.travelnode.oauth2.repository;

import com.example.travelnode.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TokenRepository extends JpaRepository<Token, Long> {
    @Query("SELECT t.user, t.accessToken, t.refreshToken FROM Token t WHERE t.user.uniqueId = ?1")
    Token findByUniqueId(String uniqueId);

    @Query("SELECT t.user, t.refreshToken FROM Token t WHERE t.refreshToken = ?1")
    Token findByRefreshToken(String refreshToken);
}
