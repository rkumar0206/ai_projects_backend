package com.rtb.cheatsheet_generator.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CheatSheetRepository extends JpaRepository<CheatSheet, Long> {

    Optional<CheatSheet> findByTechnology(String technology);
}
