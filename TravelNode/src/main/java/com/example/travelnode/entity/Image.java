package com.example.travelnode.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Table(name = "IMAGE")
public class Image {

    @Id
    @Column(name = "IMG_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imgId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "reviewId")
    private Review review;

    @JsonIgnore
    @Column(name = "IMG_NAME")
    private String imgName;

    @JsonIgnore
    @Column(name = "IMG_KEY")
    private String imgKey;

    @NotNull
    @Column(name = "IMG_URL")
    private String imgUrl;


}
