package com.github.sivdead.wx.open.dto.mapper;

import com.github.sivdead.wx.open.dao.eo.Media;
import com.github.sivdead.wx.open.dto.MediaDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MediaMapper {

    MediaMapper INSTANCE = Mappers.getMapper(MediaMapper.class);

    @Mappings(
            @Mapping(target = "groupId",source = "group.id")
    )
    MediaDto toDto(Media media);

    Media toEo(MediaDto media);
}
