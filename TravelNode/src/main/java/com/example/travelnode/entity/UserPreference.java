package com.example.travelnode.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "USER_PREFERENCE")
public class UserPreference {

    @Id
    @Column(name = "UP_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long up_Id;

    @ManyToOne
    @JoinColumn(name = "uid") // , foreignKey = @ForeignKey(name = "fk_up_uid")
    private User user;

    @Column(name="prefer_id")
    private Long prefer_id;

    @Column(name="description", length = 30)
    private String description;

    @Column(name="question_id")
    private Integer question_id;

    @Builder // 사용자가 선호하는 여행 스타일을 입력 or 수정 or 삭제할 때
    public UserPreference(User user, Long prefer_id, String description, int question_id){
        this.user = user;
        this.prefer_id = prefer_id;
        this.question_id = question_id;
        this.description = description;
    }
}
