package com.example.travelnode.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@Table(name = "TOKEN")
public class Token {
    @Id
    @Column(name = "TOKEN_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;

    @NotNull
    @OneToOne
    @JoinColumn(name = "UID") // foreignKey = @ForeignKey(name = "fk_token_uid")
    private User user;

    @NotNull
    @Column(name = "ACCESS_TOKEN")
    private String accessToken;

    @NotNull
    @Column(name = "REFRESH_TOKEN")
    private String refreshToken;

    // Access Token을 새로 생성하는 경우
    @Builder
    public Token(@NotNull User user, @NotNull String accessToken, @NotNull String refreshToken) {
        this.user = user;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    // Refresh Token만 업데이트 하는 경우
    @Builder
    public Token(@NotNull User user, @NotNull String refreshToken) {
        this.user = user;
        this.refreshToken = refreshToken;
    }
}
