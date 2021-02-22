package com.github.sivdead.wx.open.handler.base;

import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.lang.NonNull;

import java.util.Map;

public interface TypeBasedMessageHandler {

    /**
     * 获取支持的事件类型,仅在事件消息时生效
     * @return 事件类型
     */
    @NonNull
    String getSupportEventType();

    /**
     * 获取支持的消息类型
     * @return 消息类型
     */
    @NonNull
    String getSupportMsgType();


    /**
     * 处理微信推送消息.
     *
     * @param wxMessage      微信推送消息
     * @param context        上下文，如果handler或interceptor之间有信息要传递，可以用这个
     * @param wxMpService    服务类
     * @param sessionManager session管理器
     * @return xml格式的消息，如果在异步规则里处理的话，可以返回null
     * @throws WxErrorException 异常
     */
    WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                             Map<String, Object> context,
                             WxMpService wxMpService,
                             WxSessionManager sessionManager) throws WxErrorException;

}
