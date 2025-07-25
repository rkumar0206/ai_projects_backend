package com.rtb.the_random_value.colors.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "ai_app_palette_theme")
public class PaletteTheme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String themeName;
    @Column(length = 2000)
    private String rationale;

    @OneToMany(mappedBy = "paletteTheme", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaletteColor> palette;
}
