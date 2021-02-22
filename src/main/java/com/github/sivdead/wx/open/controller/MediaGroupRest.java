package com.github.sivdead.wx.open.controller;

import com.github.sivdead.wx.open.constant.ValidateGroup;
import com.github.sivdead.wx.open.dao.eo.MediaGroup;
import com.github.sivdead.wx.open.dao.repository.MediaGroupRepository;
import com.github.sivdead.wx.open.dto.MediaGroupDto;
import com.github.sivdead.wx.open.dto.mapper.MediaGroupMapper;
import com.github.sivdead.wx.open.util.ValidateUtil;
import com.github.sivdead.wx.open.web.R;
import com.querydsl.core.types.Predicate;
import me.chanjar.weixin.common.api.WxConsts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/mediaGroup")
public class MediaGroupRest {


    private final MediaGroupRepository mediaGroupRepository;
    private final MediaGroupMapper mediaGroupMapper = MediaGroupMapper.INSTANCE;

    public MediaGroupRest(MediaGroupRepository mediaGroupRepository) {
        this.mediaGroupRepository = mediaGroupRepository;
    }

    @PostMapping
    public R<MediaGroupDto> createMediaGroup(@Validated({ValidateGroup.Insert.class}) @RequestBody MediaGroupDto mediaGroupDto) {
        if (WxConsts.MaterialType.VIDEO.equals(mediaGroupDto.getMediaType())) {
            ValidateUtil.validate(mediaGroupDto, ValidateGroup.A.class);
        }
        MediaGroup mediaGroup = mediaGroupMapper.toEo(mediaGroupDto);
        mediaGroup = mediaGroupRepository.save(mediaGroup);
        return R.ok(mediaGroupMapper.toDto(mediaGroup));
    }

    @GetMapping
    public R<Page<MediaGroupDto>> queryMediaGroup(Pageable pageable,
                                                  @QuerydslPredicate(root = MediaGroup.class) Predicate predicate) {
        Page<MediaGroup> mediaGroupPage = mediaGroupRepository.findAll(predicate, pageable);
        return R.ok(mediaGroupPage.map(mediaGroupMapper::toDto));
    }

}
