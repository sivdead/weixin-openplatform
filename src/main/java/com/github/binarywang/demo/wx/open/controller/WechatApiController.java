package com.github.binarywang.demo.wx.open.controller;

import com.github.binarywang.demo.wx.open.dao.eo.OpenPlatformAccount;
import com.github.binarywang.demo.wx.open.dao.eo.ThirdPartyAccount;
import com.github.binarywang.demo.wx.open.dao.repository.OpenPlatformAccountRepository;
import com.github.binarywang.demo.wx.open.dao.repository.ThirdPartyAccountRepository;
import com.github.binarywang.demo.wx.open.service.WxOpenService;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.open.api.WxOpenComponentService;
import me.chanjar.weixin.open.bean.WxOpenCreateResult;
import me.chanjar.weixin.open.bean.auth.WxOpenAuthorizationInfo;
import me.chanjar.weixin.open.bean.auth.WxOpenAuthorizerInfo;
import me.chanjar.weixin.open.bean.result.WxOpenAuthorizerInfoResult;
import me.chanjar.weixin.open.bean.result.WxOpenQueryAuthResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;

/**
 * @author <a href="https://github.com/007gzs">007</a>
 */
@Controller
@RequestMapping("/api")
public class WechatApiController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final WxOpenService wxOpenService;
    private final OpenPlatformAccountRepository openPlatformAccountRepository;
    private final ThirdPartyAccountRepository thirdPartyAccountRepository;

    public WechatApiController(WxOpenService wxOpenService,
                               OpenPlatformAccountRepository openPlatformAccountRepository,
                               ThirdPartyAccountRepository thirdPartyAccountRepository) {
        this.wxOpenService = wxOpenService;
        this.openPlatformAccountRepository = openPlatformAccountRepository;
        this.thirdPartyAccountRepository = thirdPartyAccountRepository;
    }

    @GetMapping("/auth/goto_auth_url_show")
    @ResponseBody
    public String gotoPreAuthUrlShow() {
        return "<a href='goto_auth_url'>go</a>";
    }

    @GetMapping("/auth/goto_auth_url")
    public void gotoPreAuthUrl(HttpServletRequest request, HttpServletResponse response) {
        String host = request.getHeader("host");
        String url = "http://" + host + "/api/auth/jump";
        try {
            url = wxOpenService.getWxOpenComponentService().getPreAuthUrl(url);
            // 添加来源，解决302跳转来源丢失的问题
            response.addHeader("Referer", "http://" + host);
            response.sendRedirect(url);
        } catch (WxErrorException | IOException e) {
            logger.error("gotoPreAuthUrl", e);
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/auth/jump")
    @ResponseBody
    @Transactional(rollbackOn = Exception.class)
    public WxOpenQueryAuthResult jump(@RequestParam("auth_code") String authorizationCode) {
        try {
            WxOpenComponentService openComponentService = wxOpenService.getWxOpenComponentService();
            WxOpenQueryAuthResult queryAuthResult = openComponentService.getQueryAuth(authorizationCode);

            //获取公众号/小程序信息
            WxOpenAuthorizationInfo authorizationInfo = queryAuthResult.getAuthorizationInfo();
            String appid = authorizationInfo.getAuthorizerAppid();
            WxOpenAuthorizerInfo authorizerInfo = openComponentService
                    .getAuthorizerInfo(appid)
                    .getAuthorizerInfo();
            logger.info("authorizerInfo:{}", authorizerInfo);
            logger.info("getQueryAuth:{}", queryAuthResult);

            //根据主体查询已有的开放平台账号
            String appIdType = authorizerInfo.getMiniProgramInfo() == null ? WxConsts.AppIdType.MP_TYPE : WxConsts.AppIdType.MINI_TYPE;
            OpenPlatformAccount openPlatformAccount = openPlatformAccountRepository.findByPrincipalName(authorizerInfo.getPrincipalName());
            if (openPlatformAccount == null) {
                //调用创建并绑定接口
                WxOpenCreateResult createResult = openComponentService.createOpenAccount(appid, appIdType);
                logger.info("创建并绑定开放平台账号请求,appId:{},createResult:{}", appid, createResult);

                openPlatformAccount = OpenPlatformAccount.builder()
                        .principalName(authorizerInfo.getPrincipalName())
                        .appId(createResult.getOpenAppid())
                        .build();
                openPlatformAccount = openPlatformAccountRepository.save(openPlatformAccount);
            } else {
                Boolean bindResult = openComponentService.bindOpenAccount(appid, appIdType, openPlatformAccount.getAppId());
                logger.info("绑定开放平台账号请求,appId:{},bindResult:{}", appid, bindResult);
            }
            ThirdPartyAccount thirdPartyAccount = ThirdPartyAccount.buildFromWxOpenAuthorizerInfo(appid, authorizerInfo);
            thirdPartyAccount.setOpenPlatformAccount(openPlatformAccount);
            thirdPartyAccountRepository.save(thirdPartyAccount);

            return queryAuthResult;
        } catch (WxErrorException e) {
            logger.error("gotoPreAuthUrl", e);
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/get_authorizer_info")
    @ResponseBody
    public WxOpenAuthorizerInfoResult getAuthorizerInfo(@RequestParam String appId) {
        try {
            return wxOpenService.getWxOpenComponentService().getAuthorizerInfo(appId);
        } catch (WxErrorException e) {
            logger.error("getAuthorizerInfo", e);
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/get_access_token")
    @ResponseBody
    public String getAccessToken(@RequestParam String appId) {
        try {
            return wxOpenService.getWxOpenComponentService().getAuthorizerAccessToken(appId, false);
        } catch (WxErrorException e) {
            logger.error("getAuthorizerInfo", e);
            throw new RuntimeException(e);
        }
    }
}
