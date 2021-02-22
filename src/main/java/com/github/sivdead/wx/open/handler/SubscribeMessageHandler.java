package com.github.sivdead.wx.open.handler;

import cn.hutool.core.util.StrUtil;
import com.github.sivdead.wx.open.dao.eo.ThirdPartyAccount;
import com.github.sivdead.wx.open.dao.eo.ThirdPartyUser;
import com.github.sivdead.wx.open.dao.repository.ThirdPartyAccountRepository;
import com.github.sivdead.wx.open.dao.repository.ThirdPartyUserRepository;
import com.github.sivdead.wx.open.dto.mapper.ThirdPartyUserMapper;
import com.github.sivdead.wx.open.handler.base.TypeBasedMessageHandler;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SubscribeMessageHandler implements TypeBasedMessageHandler {


    private final ThirdPartyUserRepository thirdPartyUserRepository;
    private final ThirdPartyAccountRepository thirdPartyAccountRepository;
    private final ThirdPartyUserMapper thirdPartyUserMapper = ThirdPartyUserMapper.INSTANCE;

    public SubscribeMessageHandler(ThirdPartyUserRepository thirdPartyUserRepository,
                                   ThirdPartyAccountRepository thirdPartyAccountRepository) {
        this.thirdPartyUserRepository = thirdPartyUserRepository;
        this.thirdPartyAccountRepository = thirdPartyAccountRepository;
    }

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context,
                                    WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        String userOpenid = wxMessage.getFromUser();
        if (StrUtil.isNotBlank(userOpenid)) {

            //根据openid查询客户信息
            ThirdPartyAccount thirdPartyAccount = thirdPartyAccountRepository.findById(wxMpService.getWxMpConfigStorage().getAppId())
                    .orElse(null);
            if (thirdPartyAccount != null) {
                WxMpUser wxMpUser = wxMpService.getUserService().userInfo(userOpenid);

                ThirdPartyUser thirdPartyUser = thirdPartyUserMapper.fromWxMpUser(wxMpUser);
                thirdPartyUser.setThirdPartyAccount(thirdPartyAccount);
                thirdPartyUserRepository.save(thirdPartyUser);
            }
        }

        return null;
    }

    @Override
    @NonNull
    public String getSupportEventType() {
        return WxConsts.EventType.SUBSCRIBE;
    }

    @Override
    @NonNull
    public String getSupportMsgType() {
        return WxConsts.XmlMsgType.EVENT;
    }
}
