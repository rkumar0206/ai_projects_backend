package com.rtb.code_equivalent.db;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "ai_app_code_equivalent",
        uniqueConstraints = @UniqueConstraint(columnNames = {"source", "target", "tool"})

)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeEquivalent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source", nullable = false)
    private String source;

    @Column(name = "target", nullable = false)
    private String target;

    @Column(name = "tool", nullable = false)
    private String tool;

    @Column(name = "report", nullable = false, columnDefinition = "TEXT")
    private String reportMarkdown;

}
