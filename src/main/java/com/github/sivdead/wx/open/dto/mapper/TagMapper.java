package com.github.sivdead.wx.open.dto.mapper;

import com.github.sivdead.wx.open.dao.eo.Tag;
import com.github.sivdead.wx.open.dto.TagDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TagMapper {

    TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);

    TagDto toDto(Tag tag);

    void updateTagFromDto(TagDto tagDto, @MappingTarget Tag tag);

    Tag toEo(TagDto tagDto);
}
