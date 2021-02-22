package com.github.sivdead.wx.open.dto.mapper;

import com.github.sivdead.wx.open.dao.eo.Article;
import com.github.sivdead.wx.open.dao.eo.Media;
import com.github.sivdead.wx.open.dto.ArticleDto;
import com.github.sivdead.wx.open.dto.MediaDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ArticleMapper {


    ArticleMapper INSTANCE = Mappers.getMapper(ArticleMapper.class);

    @Mappings(
            @Mapping(target = "thumbMediaId",source = "thumbMedia.id")
    )
    ArticleDto toDto(Article article);

    Article toEo(ArticleDto media);
}
