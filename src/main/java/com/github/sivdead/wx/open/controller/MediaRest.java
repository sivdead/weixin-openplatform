package com.github.sivdead.wx.open.controller;


import com.github.sivdead.wx.open.constant.ValidateGroup;
import com.github.sivdead.wx.open.dao.eo.Media;
import com.github.sivdead.wx.open.dao.eo.MediaGroup;
import com.github.sivdead.wx.open.dao.repository.MediaGroupRepository;
import com.github.sivdead.wx.open.dao.repository.MediaRepository;
import com.github.sivdead.wx.open.dto.MediaDto;
import com.github.sivdead.wx.open.dto.mapper.MediaMapper;
import com.github.sivdead.wx.open.util.ValidateUtil;
import com.github.sivdead.wx.open.web.R;
import com.querydsl.core.types.Predicate;
import me.chanjar.weixin.common.api.WxConsts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.github.sivdead.wx.open.exception.ExceptionCode.MEDIA_GROUP_NOT_FOUND;

@RestController
@RequestMapping("/api/v1/media")
public class MediaRest {

    private final MediaRepository mediaRepository;
    private final MediaMapper mediaMapper = MediaMapper.INSTANCE;
    private final MediaGroupRepository mediaGroupRepository;

    public MediaRest(MediaRepository mediaRepository, MediaGroupRepository mediaGroupRepository) {
        this.mediaRepository = mediaRepository;
        this.mediaGroupRepository = mediaGroupRepository;
    }

    @PostMapping
    public R<MediaDto> createMedia(@Validated @RequestBody MediaDto mediaDto) {
        if (WxConsts.MaterialType.VIDEO.equals(mediaDto.getType())) {
            ValidateUtil.validate(mediaDto, ValidateGroup.A.class);
        }
        MediaGroup mediaGroup = mediaGroupRepository.findById(mediaDto.getGroupId()).orElse(null);
        if (mediaGroup == null) {
            throw MEDIA_GROUP_NOT_FOUND.toBusinessException();
        }
        Media media = mediaMapper.toEo(mediaDto);
        media.setGroup(mediaGroup);
        media = mediaRepository.save(media);
        return R.ok(mediaMapper.toDto(media));
    }

    @GetMapping("/{id}")
    public R<MediaDto> getMediaById(@PathVariable("id") Media media) {
        return R.ok(mediaMapper.toDto(media));
    }

    @GetMapping
    public R<Page<MediaDto>> queryMedia(Pageable pageable,
                                        @QuerydslPredicate(root = Media.class) Predicate predicate) {
        Page<Media> mediaPage = mediaRepository.findAll(predicate, pageable);
        return R.ok(mediaPage.map(mediaMapper::toDto));
    }
}
