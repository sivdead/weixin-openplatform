package com.github.sivdead.wx.open.handler;

import com.github.sivdead.wx.open.dao.repository.ThirdPartyUserRepository;
import com.github.sivdead.wx.open.handler.base.TypeBasedMessageHandler;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UnsubscribeMessageHandler implements TypeBasedMessageHandler {

    private final ThirdPartyUserRepository thirdPartyUserRepository;

    public UnsubscribeMessageHandler(ThirdPartyUserRepository thirdPartyUserRepository) {
        this.thirdPartyUserRepository = thirdPartyUserRepository;
    }

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context,
                                    WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {

        thirdPartyUserRepository.findById(wxMessage.getFromUser())
                .ifPresent(user -> {
                    user.setSubscribe(false);
                    thirdPartyUserRepository.save(user);
                });
        return null;
    }

    @Override
    @NonNull
    public String getSupportEventType() {
        return WxConsts.EventType.UNSUBSCRIBE;
    }

    @Override
    @NonNull
    public String getSupportMsgType() {
        return WxConsts.XmlMsgType.EVENT;
    }
}
