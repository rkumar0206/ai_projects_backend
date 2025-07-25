package com.rtb.the_random_value.colors.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "ai_app_palette_color")
public class PaletteColor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hexCode;
    private String colorName;
    private String category;

    @ManyToOne
    @JoinColumn(name = "palette_theme_id")
    private PaletteTheme paletteTheme;
}