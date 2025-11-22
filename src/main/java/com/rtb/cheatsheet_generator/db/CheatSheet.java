package com.rtb.cheatsheet_generator.db;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ai_app_cheat_sheet")
public class CheatSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String technology;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String cheatSheetMarkdown;
}
