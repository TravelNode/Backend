package com.example.travelnode.repository;

import com.example.travelnode.entity.PreferenceList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferenceListRepository extends JpaRepository<PreferenceList, Long> {

}
