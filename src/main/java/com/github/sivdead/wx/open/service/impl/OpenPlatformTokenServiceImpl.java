package com.github.sivdead.wx.open.service.impl;

import com.github.sivdead.wx.open.exception.BusinessException;
import com.github.sivdead.wx.open.service.ITokenService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.open.api.WxOpenService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OpenPlatformTokenServiceImpl implements ITokenService {

    private final WxOpenService wxOpenService;

    public OpenPlatformTokenServiceImpl(WxOpenService wxOpenService) {
        this.wxOpenService = wxOpenService;
    }

    @Override
    public String getAccessToken(String appId) {
        try {
            return wxOpenService.getWxOpenComponentService().getAuthorizerAccessToken(appId, false);
        } catch (WxErrorException e) {
            log.error("获取微信accessToken失败",e);
            throw new BusinessException(e.getError().getErrorCode(),e.getError().getErrorMsg());
        }
    }
}
