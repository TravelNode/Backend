package com.example.travelnode.entity;

import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Table(name = "KEYWORD_LIST")
public class KeywordList {

    @Id
    @Column(name = "KEY_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long keyId;

    @NotNull
    @Column(name = "KEYWORD")
    private String keyword;
}
