package com.github.sivdead.wx.open.dto.mapper;

import com.github.sivdead.wx.open.dao.eo.MenuTemplate;
import com.github.sivdead.wx.open.dto.MenuTemplateDto;
import org.mapstruct.Mapper;

@Mapper
public interface MenuTemplateMapper {


    MenuTemplateDto toDto(MenuTemplate menuTemplate);

    MenuTemplate toEo(MenuTemplateDto menuTemplateDto);

}
