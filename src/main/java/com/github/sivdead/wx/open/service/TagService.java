package com.github.sivdead.wx.open.service;

import com.github.sivdead.wx.open.dao.eo.*;
import com.github.sivdead.wx.open.dao.repository.TagRepository;
import com.github.sivdead.wx.open.dao.repository.WxTagRepository;
import com.github.sivdead.wx.open.dto.TagDto;
import com.github.sivdead.wx.open.dto.mapper.TagMapper;
import com.github.sivdead.wx.open.web.R;
import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpUserTagService;
import me.chanjar.weixin.mp.bean.tag.WxUserTag;
import me.chanjar.weixin.open.api.WxOpenComponentService;
import me.chanjar.weixin.open.api.WxOpenMpService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.validation.constraints.NotNull;

import static com.github.sivdead.wx.open.exception.ExceptionCode.TAG_NAME_DUPLICATED;
import static com.github.sivdead.wx.open.exception.ExceptionCode.TAG_NOT_FOUND;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class TagService {


    private final WxOpenComponentService wxOpenComponentService;
    private final TagMapper tagMapper = TagMapper.INSTANCE;
    private final TagRepository tagRepository;
    private final WxTagRepository wxTagRepository;

    public TagService(WxOpenComponentService wxOpenComponentService,
                      TagRepository tagRepository,
                      WxTagRepository wxTagRepository) {
        this.wxOpenComponentService = wxOpenComponentService;
        this.tagRepository = tagRepository;
        this.wxTagRepository = wxTagRepository;
    }

    public TagDto addTag(TagDto tagDto) {

        //重复校验
        if (tagRepository.count(QTag.tag.name.eq(tagDto.getName())) > 1) {
            throw TAG_NAME_DUPLICATED.toBusinessException();
        }

        Tag tag = tagMapper.toEo(tagDto);
        tag = tagRepository.save(tag);
        tagDto.setId(tag.getId());
        return tagDto;
    }

    public TagDto updateTag(TagDto tagDto, @NotNull Long id) throws WxErrorException {
        Tag tag = tagRepository.findById(id).orElseThrow(TAG_NOT_FOUND::toBusinessException);

        //重复校验
        if (tagRepository.count(
                QTag.tag.name.eq(tagDto.getName())
                        .and(QTag.tag.id.ne(id))
        ) > 1) {
            throw TAG_NAME_DUPLICATED.toBusinessException();
        }

        tagMapper.updateTagFromDto(tagDto, tag);

        //更新微信的标签
        for (WxTag wxTag : tag.getWxTags()) {
            WxMpUserTagService userTagService = wxOpenComponentService.getWxMpServiceByAppid(wxTag.getAppId()).getUserTagService();
            userTagService.tagUpdate(wxTag.getWxTagId(), tagDto.getName());
        }

        tagRepository.save(tag);
        tagDto.setId(id);
        return tagDto;
    }

    public WxTag applyTag2Wx(@NotNull Tag tag, @NotNull ThirdPartyAccount thirdPartyAccount) throws WxErrorException {
        WxMpUserTagService userTagService = wxOpenComponentService.getWxMpServiceByAppid(thirdPartyAccount.getAppId())
                .getUserTagService();
        WxUserTag wxUserTag = userTagService.tagCreate(tag.getName());
        WxTag wxTag = WxTag.builder()
                .tag(tag)
                .wxTagId(wxUserTag.getId())
                .thirdPartyAccount(thirdPartyAccount)
                .build();
        return wxTagRepository.save(wxTag);
    }

    public Page<TagDto> queryTag(Pageable pageable,Predicate predicate){
        Page<Tag> tagPage = tagRepository.findAll(predicate, pageable);
        return tagPage.map(tagMapper::toDto);
    }

    public void deleteTag(@NotNull Long id) throws WxErrorException {

        Tag tag = tagRepository.findById(id).orElse(null);
        if (tag != null) {
            for (WxTag wxTag : tag.getWxTags()) {
                WxOpenMpService wxOpenMpService = wxOpenComponentService.getWxMpServiceByAppid(wxTag.getAppId());
                wxOpenMpService.getUserTagService().tagDelete(wxTag.getWxTagId());
            }
            tagRepository.deleteById(id);
        }
    }
}
