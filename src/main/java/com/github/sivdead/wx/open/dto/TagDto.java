package com.github.sivdead.wx.open.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TagDto {

    private Long id;

    @NotBlank
    private String name;
}
