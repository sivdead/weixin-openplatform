package com.github.sivdead.wx.open.service;

import com.github.sivdead.wx.open.dao.eo.Tag;
import com.github.sivdead.wx.open.dao.eo.ThirdPartyAccount;
import com.github.sivdead.wx.open.dao.eo.ThirdPartyUser;
import com.github.sivdead.wx.open.dao.eo.WxTag;
import com.github.sivdead.wx.open.dao.repository.TagRepository;
import com.github.sivdead.wx.open.dao.repository.ThirdPartyUserRepository;
import com.github.sivdead.wx.open.dao.repository.WxTagRepository;
import com.github.sivdead.wx.open.dto.ThirdPartyUserDto;
import com.github.sivdead.wx.open.dto.mapper.ThirdPartyUserMapper;
import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpUserTagService;
import me.chanjar.weixin.open.api.WxOpenComponentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

import static com.github.sivdead.wx.open.exception.ExceptionCode.TAG_NOT_FOUND;
import static com.github.sivdead.wx.open.exception.ExceptionCode.THIRD_PARTY_USER_NOT_FOUND;

@Service
@Slf4j
public class ThirdPartyUserService {


    private final ThirdPartyUserRepository thirdPartyUserRepository;
    private final WxOpenComponentService wxOpenComponentService;
    private final TagRepository tagRepository;
    private final WxTagRepository wxTagRepository;
    private final TagService tagService;
    private final ThirdPartyUserMapper thirdPartyUserMapper = ThirdPartyUserMapper.INSTANCE;


    public ThirdPartyUserService(ThirdPartyUserRepository thirdPartyUserRepository,
                                 WxOpenComponentService wxOpenComponentService,
                                 TagRepository tagRepository,
                                 WxTagRepository wxTagRepository,
                                 TagService tagService) {
        this.thirdPartyUserRepository = thirdPartyUserRepository;
        this.wxOpenComponentService = wxOpenComponentService;
        this.tagRepository = tagRepository;
        this.wxTagRepository = wxTagRepository;
        this.tagService = tagService;
    }

    public void tagUser(@NotNull String userOpenid, @NotNull Long tagId) throws WxErrorException {
        ThirdPartyUser thirdPartyUser = thirdPartyUserRepository.findById(userOpenid)
                .orElseThrow(THIRD_PARTY_USER_NOT_FOUND::toBusinessException);

        //判断用户是否已有该标签
        if (thirdPartyUser.getTags()
                .stream()
                .anyMatch(e -> tagId.equals(e.getId()))) {
            return;
        }

        ThirdPartyAccount thirdPartyAccount = thirdPartyUser.getThirdPartyAccount();
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(TAG_NOT_FOUND::toBusinessException);
        //找到对应的wxTag
        WxTag wxTag = wxTagRepository.findByTagIdAndAppId(tag.getId(), thirdPartyAccount.getAppId());
        if (wxTag == null) {
            wxTag = tagService.applyTag2Wx(tag, thirdPartyAccount);
        }
        WxMpUserTagService userTagService = wxOpenComponentService.getWxMpServiceByAppid(thirdPartyAccount.getAppId())
                .getUserTagService();
        userTagService.batchTagging(wxTag.getWxTagId(), new String[]{thirdPartyUser.getOpenid()});

        thirdPartyUser.getTags().add(tag);
        thirdPartyUserRepository.save(thirdPartyUser);
    }

    public Page<ThirdPartyUserDto> queryThirdPartyUser(Predicate predicate, Pageable pageable) {
        Page<ThirdPartyUser> tagPage = thirdPartyUserRepository.findAll(predicate, pageable);
        return tagPage.map(thirdPartyUserMapper::toDto);
    }
}
