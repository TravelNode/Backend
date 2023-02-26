package com.example.travelnode.S3.uploadtest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface S3ImageRepository extends JpaRepository<S3Image, Long> {
}
