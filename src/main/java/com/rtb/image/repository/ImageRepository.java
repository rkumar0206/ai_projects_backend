package com.rtb.image.repository;

import com.rtb.image.dto.PartialImageResponse;
import com.rtb.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByPromptIgnoreCase(String prompt);

//    @Query("SELECT new com.rtb.image.dto.PartialImageResponse(i.id, i.prompt) FROM ai_app_image i WHERE i.prompt NOT IN (SELECT r.prompt FROM ai_app_recipe r) ORDER BY i.id DESC")
//    List<PartialImageResponse> findAllPrompts();

    @Query("""
    SELECT new com.rtb.image.dto.PartialImageResponse(i.id, i.prompt)
    FROM ai_app_image i
    WHERE i.prompt NOT IN (SELECT r.imagePrompt FROM ai_app_recipe r)
    ORDER BY i.id DESC
    """)
    List<PartialImageResponse> findAllPromptsExceptFromTheRecipe();

    @Modifying
    void deleteByPrompt(String prompt);
}
