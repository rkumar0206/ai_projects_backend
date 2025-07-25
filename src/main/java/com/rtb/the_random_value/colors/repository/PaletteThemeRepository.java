package com.rtb.the_random_value.colors.repository;

import com.rtb.the_random_value.colors.entity.PaletteTheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaletteThemeRepository extends JpaRepository<PaletteTheme, Long> {

    Optional<PaletteTheme> findByThemeNameIgnoreCase(String themeName);
}
