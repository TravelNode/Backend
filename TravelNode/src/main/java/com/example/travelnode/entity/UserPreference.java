//package com.example.travelnode.entity;
//
//import lombok.AccessLevel;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//import javax.validation.constraints.NotNull;
//
//@Entity
//@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Table(name = "USER_PREFERENCE")
//public class UserPreference {
//
//    @Id
//    @Column(name = "UP_ID")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long upId;
//
//    @NotNull
//    @ManyToOne
//    @JoinColumn(name = "uid") // , foreignKey = @ForeignKey(name = "fk_up_uid")
//    private User user;
//
//    @NotNull
//    @ManyToOne
//    @JoinColumn(name = "prefer_Id") // , foreignKey = @ForeignKey(name = "fk_up_preferId")
//    private PreferenceList preferenceList;
//
//    @Builder // 사용자가 선호하는 여행 스타일을 입력 or 수정 or 삭제할 때
//    public UserPreference(@NotNull User user, @NotNull PreferenceList preferenceList){
//        this.user = user;
//        this.preferenceList = preferenceList;
//    }
//}
