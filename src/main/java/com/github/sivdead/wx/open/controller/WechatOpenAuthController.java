package com.github.sivdead.wx.open.controller;

import com.github.sivdead.wx.open.dao.eo.OpenPlatformAccount;
import com.github.sivdead.wx.open.dao.eo.ThirdPartyAccount;
import com.github.sivdead.wx.open.dao.repository.OpenPlatformAccountRepository;
import com.github.sivdead.wx.open.dao.repository.ThirdPartyAccountRepository;
import com.github.sivdead.wx.open.exception.BusinessException;
import com.github.sivdead.wx.open.service.ITokenService;
import com.github.sivdead.wx.open.web.R;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.open.api.WxOpenComponentService;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.bean.WxOpenCreateResult;
import me.chanjar.weixin.open.bean.WxOpenGetResult;
import me.chanjar.weixin.open.bean.auth.WxOpenAuthorizationInfo;
import me.chanjar.weixin.open.bean.auth.WxOpenAuthorizerInfo;
import me.chanjar.weixin.open.bean.result.WxOpenAuthorizerInfoResult;
import me.chanjar.weixin.open.bean.result.WxOpenQueryAuthResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;

import static com.github.sivdead.wx.open.exception.ExceptionCode.ALREADY_BIND_OTHER_OPEN_PLATFORM;
import static me.chanjar.weixin.common.error.WxMaErrorMsgEnum.CODE_89002;

/**
 * @author <a href="https://github.com/007gzs">007</a>
 */
@RestController
@Slf4j
@RequestMapping("/api/open/auth")
public class WechatOpenAuthController {
    private final WxOpenService wxOpenService;
    private final OpenPlatformAccountRepository openPlatformAccountRepository;
    private final ThirdPartyAccountRepository thirdPartyAccountRepository;
    private final ITokenService tokenService;


    public WechatOpenAuthController(WxOpenService wxOpenService,
                                    OpenPlatformAccountRepository openPlatformAccountRepository,
                                    ThirdPartyAccountRepository thirdPartyAccountRepository, ITokenService tokenService) {
        this.wxOpenService = wxOpenService;
        this.openPlatformAccountRepository = openPlatformAccountRepository;
        this.thirdPartyAccountRepository = thirdPartyAccountRepository;
        this.tokenService = tokenService;
    }

    @GetMapping("/goto_auth_url_show")
    @ResponseBody
    public String gotoPreAuthUrlShow() {
        return "<a href='goto_auth_url'>go</a>";
    }

    @GetMapping("/goto_auth_url")
    public void gotoPreAuthUrl(HttpServletRequest request, HttpServletResponse response) {
        String host = request.getHeader("host");
        String url = "http://" + host + "/api/open/auth/jump";
        try {
            url = wxOpenService.getWxOpenComponentService().getPreAuthUrl(url);
            // 添加来源，解决302跳转来源丢失的问题
            response.addHeader("Referer", "http://" + host);
            response.sendRedirect(url);
        } catch (WxErrorException e) {
            log.error("gotoPreAuthUrl", e);
            throw new BusinessException(e);
        } catch (IOException e) {
            log.error("gotoPreAuthUrl", e);
            throw new BusinessException(e.getMessage());
        }
    }

    @GetMapping("/jump")
    @ResponseBody
    @Transactional(rollbackOn = Exception.class)
    public R<WxOpenQueryAuthResult> jump(@RequestParam("auth_code") String authorizationCode) {
        try {
            WxOpenComponentService openComponentService = wxOpenService.getWxOpenComponentService();
            WxOpenQueryAuthResult queryAuthResult = openComponentService.getQueryAuth(authorizationCode);

            //获取公众号/小程序信息
            WxOpenAuthorizationInfo authorizationInfo = queryAuthResult.getAuthorizationInfo();
            String appid = authorizationInfo.getAuthorizerAppid();
            WxOpenAuthorizerInfo authorizerInfo = openComponentService
                    .getAuthorizerInfo(appid)
                    .getAuthorizerInfo();
            log.info("authorizerInfo:{}", authorizerInfo);
            log.info("getQueryAuth:{}", queryAuthResult);

            //根据主体查询已有的开放平台账号
            String appIdType = authorizerInfo.getMiniProgramInfo() == null ? WxConsts.AppIdType.MP_TYPE : WxConsts.AppIdType.MINI_TYPE;
            OpenPlatformAccount openPlatformAccount = null;
            //FIXME 只有授权了开放平台帐号管理权限才能绑定,目前是写死权限
            if (authorizationInfo.getFuncInfo().contains(24)) {
                openPlatformAccount = openPlatformAccountRepository.findByPrincipalName(authorizerInfo.getPrincipalName());

                WxOpenGetResult openAccount = getExistOpenAccount(openComponentService, appid, appIdType);
                //已绑定,且绑定的不是该第三方平台的开放平台
                if ((openPlatformAccount == null && openAccount != null && openAccount.getOpenAppid() != null)
                        || (openPlatformAccount != null && !openAccount.getOpenAppid().equals(openPlatformAccount.getAppId()))) {
                    throw ALREADY_BIND_OTHER_OPEN_PLATFORM.toBusinessException();
                }

                if (openPlatformAccount == null) {
                    //调用创建并绑定接口
                    WxOpenCreateResult createResult = openComponentService.createOpenAccount(appid, appIdType);
                    log.info("创建并绑定开放平台账号请求,appId:{},createResult:{}", appid, createResult);

                    openPlatformAccount = OpenPlatformAccount.builder()
                            .principalName(authorizerInfo.getPrincipalName())
                            .appId(createResult.getOpenAppid())
                            .build();
                    openPlatformAccount = openPlatformAccountRepository.save(openPlatformAccount);
                } else {
                    //未绑定时才绑定
                    if (openAccount.getOpenAppid() == null) {
                        Boolean bindResult = openComponentService.bindOpenAccount(appid, appIdType, openPlatformAccount.getAppId());
                        log.info("绑定开放平台账号请求,appId:{},bindResult:{}", appid, bindResult);
                    }
                }
            }
            ThirdPartyAccount thirdPartyAccount = ThirdPartyAccount.buildFromWxOpenAuthorizerInfo(appid, authorizerInfo);
            thirdPartyAccount.setOpenPlatformAccount(openPlatformAccount);
            //refresh Token放到缓存
//            openComponentService.getWxOpenConfigStorage().setAuthorizerRefreshToken(appid, authorizationInfo.getAuthorizerRefreshToken());
            thirdPartyAccountRepository.save(thirdPartyAccount);

            return R.ok(queryAuthResult);
        } catch (WxErrorException e) {
            log.error("gotoPreAuthUrl", e);
            throw new BusinessException(e);
        }
    }

    private WxOpenGetResult getExistOpenAccount(WxOpenComponentService openComponentService, String appid, String appIdType) throws WxErrorException {
        //检查已有的绑定
        WxOpenGetResult openAccount;
        try {
            openAccount = openComponentService.getOpenAccount(appid, appIdType);
        } catch (WxErrorException e) {
            if (e.getError().getErrorCode() == CODE_89002.getCode()) {
                openAccount = null;
            } else {
                throw e;
            }
        }
        return openAccount;
    }

    @GetMapping("/get_authorizer_info")
    @ResponseBody
    public R<WxOpenAuthorizerInfoResult> getAuthorizerInfo(@RequestParam String appId) {
        try {
            return R.ok(wxOpenService.getWxOpenComponentService().getAuthorizerInfo(appId));
        } catch (WxErrorException e) {
            log.error("getAuthorizerInfo", e);
            throw new BusinessException(e);
        }
    }

    @GetMapping("/get_access_token")
    @ResponseBody
    public R<String> getAccessToken(@RequestParam String appId) {
        return R.ok(tokenService.getAccessToken(appId));
    }
}
