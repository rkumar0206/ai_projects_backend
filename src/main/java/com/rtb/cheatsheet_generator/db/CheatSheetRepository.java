package com.rtb.cheatsheet_generator.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CheatSheetRepository extends JpaRepository<CheatSheet, Long> {

    Optional<CheatSheet> findByTechnology(String technology);

    @Query("select cs.technology from CheatSheet cs")
    List<String> findAllTechnologies();

    void deleteByTechnology(String technology);
}
