package com.example.travelnode.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
@Table(name = "USER")
public class User {

    @Id
    @Column(name = "UID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @NotNull
    @Column(name = "UNIQUE_ID", unique = true)
    private String uniqueId;

    @NotNull
    @Size(max = 128)
    @Column(name = "EMAIL", length = 128, unique = true)
    private String email;

    @NotNull
    @Size(max = 10)
    @Column(name = "NICKNAME", length = 10)
    private String nickname;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE_TYPE", length = 20)
    private RoleType roleType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "PROVIDER_TYPE", length = 10)
    private ProviderType providerType;

    @NotNull
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "MODIFIED_AT")
    private LocalDateTime modifiedAt;

//    // @NotNull
//    @ManyToOne
//    @JoinColumn(name = "AVATAR_ID") // foreignKey = @ForeignKey(name = "fk_user_avatarId")
//    private Avatar avatar;

    // 여행 성향
    @OneToMany(mappedBy = "user")
    private List<UserPreference> prefer_list = new ArrayList<>();
    public void addPrefer(UserPreference up){
        this.prefer_list.add(up);
    }

    @NotNull
    @ColumnDefault("0")
    @Column(name = "TRAVEL_COUNT")
    private Integer travelCount;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "LEVEL")
    private Integer level;

    // 여행 성향 추가
    @Builder
    public User( // 새로운 유저가 가입하는 경우
                 @NotNull @Size(max = 128) String uniqueId, String email, @NotNull @Size(max = 10) String nickname,
                 @NotNull RoleType roleType, @NotNull ProviderType providerType, @NotNull LocalDateTime createdAt,
                 @NotNull LocalDateTime modifiedAt, Integer travelCount, Integer level, List<UserPreference> prefer_list) {
        this.uniqueId = uniqueId;
        this.email = email != null ? email : "NO_EMAIL";
        this.nickname = nickname;
        this.roleType = roleType;
        this.providerType = providerType;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        // @NotNull Avatar avatar
        // this.avatar = avatar;\
        this.prefer_list = prefer_list;
        this.travelCount = travelCount != null ? travelCount : 0;
        this.level = level != null ? level : 1;
    }

    @Builder
    public User(@NotNull @Size(max = 10) String nickname, LocalDateTime modifiedAt) { // 닉네임을 변경하는 경우
        this.nickname = nickname;
        this.modifiedAt = modifiedAt;
    }

    @Builder
    public User(@NotNull Integer travelCount, @NotNull Integer level) { // 여행을 완료하여 여행 횟수&레벨을 변경하는 경우
        this.travelCount = travelCount;
        this.level = level;
    }

    public void changeRole(@NotNull RoleType roleType) { // 유저 권한 변경
        this.roleType = roleType;
    }
}
