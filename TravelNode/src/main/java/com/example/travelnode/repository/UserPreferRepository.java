package com.example.travelnode.repository;

import com.example.travelnode.entity.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPreferRepository extends JpaRepository<UserPreference, Long> {
}
