package com.rtb.the_random_value.colors.mapper;

import com.rtb.the_random_value.colors.dto.PaletteThemeDTO;
import com.rtb.the_random_value.colors.entity.PaletteTheme;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface PaletteThemeMapper {

    PaletteThemeDTO toDTO(PaletteTheme paletteTheme);

    PaletteTheme toEntity(PaletteThemeDTO paletteThemeDTO);
}
