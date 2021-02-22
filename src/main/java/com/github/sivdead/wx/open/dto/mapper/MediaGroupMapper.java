package com.github.sivdead.wx.open.dto.mapper;

import com.github.sivdead.wx.open.dao.eo.MediaGroup;
import com.github.sivdead.wx.open.dto.MediaGroupDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

@Mapper
public interface MediaGroupMapper {

    MediaGroupMapper INSTANCE = Mappers.getMapper(MediaGroupMapper.class);

    MediaGroupDto toDto(MediaGroup mediaGroup);

    MediaGroup toEo(MediaGroupDto mediaGroupDto);

}
