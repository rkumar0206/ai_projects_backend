package com.rtb.code_equivalent.db;

import com.rtb.code_equivalent.dto.CodeEquivalentResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CodeEquivalentRepository extends JpaRepository<CodeEquivalent, Long> {

    Optional<CodeEquivalent> findBySourceAndTargetAndTool(String source, String target, String tool);

    @Query("""
                select new com.rtb.code_equivalent.dto.CodeEquivalentResponseDTO(
                    ce.id, ce.source, ce.target, ce.tool
                ) from CodeEquivalent ce
            """)
    List<CodeEquivalentResponseDTO> findAllExceptReport();

}
