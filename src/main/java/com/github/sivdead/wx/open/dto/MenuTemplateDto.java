package com.github.sivdead.wx.open.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuTemplateDto {

    @NotBlank
    private String name;

    private Long id;

    @Max(3)
    private List<MenuItemDto> menuItems;

}
