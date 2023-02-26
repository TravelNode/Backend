package com.example.travelnode.S3.uploadtest;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "s3image")
public class S3Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long s3image_id;

    @Column(name = "image_url")
    private String imageUrl;
}
