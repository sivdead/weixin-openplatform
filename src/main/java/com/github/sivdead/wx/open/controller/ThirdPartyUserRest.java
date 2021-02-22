package com.github.sivdead.wx.open.controller;

import com.github.sivdead.wx.open.dao.eo.ThirdPartyUser;
import com.github.sivdead.wx.open.dto.ThirdPartyUserDto;
import com.github.sivdead.wx.open.service.ThirdPartyUserService;
import com.github.sivdead.wx.open.web.R;
import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/thirdPartyUser")
public class ThirdPartyUserRest {


    private final ThirdPartyUserService thirdPartyUserService;

    public ThirdPartyUserRest(ThirdPartyUserService thirdPartyUserService) {
        this.thirdPartyUserService = thirdPartyUserService;
    }

    @PostMapping("/{userOpenId}/tag")
    public R<Void> tagUser(@PathVariable String userOpenId,Long tagId) throws WxErrorException {
        thirdPartyUserService.tagUser(userOpenId, tagId);
        return R.ok();
    }

    @GetMapping
    public R<Page<ThirdPartyUserDto>> queryThirdPartyUser(Pageable pageable,
                                                 @QuerydslPredicate(root = ThirdPartyUser.class) Predicate predicate) {
        return R.ok(thirdPartyUserService.queryThirdPartyUser(predicate, pageable));
    }
}
