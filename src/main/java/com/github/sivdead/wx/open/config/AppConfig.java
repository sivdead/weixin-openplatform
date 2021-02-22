package com.github.sivdead.wx.open.config;

import com.github.sivdead.wx.open.handler.base.TypeBasedMessageHandler;
import io.minio.MinioClient;
import io.minio.policy.PolicyType;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.redis.RedisTemplateWxRedisOps;
import me.chanjar.weixin.common.redis.WxRedisOps;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.open.api.WxOpenComponentService;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.api.impl.WxOpenInRedisConfigStorage;
import me.chanjar.weixin.open.api.impl.WxOpenMessageRouter;
import me.chanjar.weixin.open.api.impl.WxOpenServiceImpl;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import static com.github.sivdead.wx.open.constant.AppConstant.REDIS_KEY_PREFIX;

@Configuration
@EnableConfigurationProperties({WechatOpenProperties.class, MinioProperties.class})
@Slf4j
public class AppConfig {

    @Bean
    public WxOpenService wxOpenService(StringRedisTemplate stringRedisTemplate,
                                       WechatOpenProperties wechatOpenProperties) {
        WxOpenService wxOpenService = new WxOpenServiceImpl();
        WxRedisOps redisOps = new RedisTemplateWxRedisOps(stringRedisTemplate);
        WxOpenInRedisConfigStorage inRedisConfigStorage = new WxOpenInRedisConfigStorage(redisOps, REDIS_KEY_PREFIX);
        inRedisConfigStorage.setComponentAppId(wechatOpenProperties.getComponentAppId());
        inRedisConfigStorage.setComponentAppSecret(wechatOpenProperties.getComponentSecret());
        inRedisConfigStorage.setComponentToken(wechatOpenProperties.getComponentToken());
        inRedisConfigStorage.setComponentAesKey(wechatOpenProperties.getComponentAesKey());
        wxOpenService.setWxOpenConfigStorage(inRedisConfigStorage);
        return wxOpenService;
    }

    @Bean
    public WxOpenComponentService wxOpenComponentService(WxOpenService wxOpenService){
        return wxOpenService.getWxOpenComponentService();
    }

    @Bean
    public WxOpenMessageRouter wxOpenMessageRouter(ObjectProvider<WxMpMessageHandler> wxMpMessageHandlers,
                                                   ObjectProvider<TypeBasedMessageHandler> typeBasedMessageHandlers,
                                                   WxOpenService wxOpenService) {
        WxOpenMessageRouter wxOpenMessageRouter = new WxOpenMessageRouter(wxOpenService);

        for (WxMpMessageHandler mpMessageHandler : wxMpMessageHandlers) {
            wxOpenMessageRouter.rule()
                    .handler(mpMessageHandler)
                    .next();
        }
        for (TypeBasedMessageHandler messageHandler : typeBasedMessageHandlers) {
            wxOpenMessageRouter.rule()
                    .msgType(messageHandler.getSupportMsgType())
                    .event(messageHandler.getSupportEventType())
                    .handler(messageHandler::handle)
                    .next();
        }

        return wxOpenMessageRouter;
    }

    @Bean
    public WxMpMessageHandler wxMpMessageHandler() {
        return (wxMpXmlMessage, map, wxMpService, wxSessionManager) -> {
            log.info("\n接收到 {} 公众号请求消息，内容：{}", wxMpService.getWxMpConfigStorage().getAppId(), wxMpXmlMessage);
            return null;
        };
    }

    @Bean
    public MinioClient minioClient(MinioProperties minioProperties) throws Exception {
        MinioClient minioClient = new MinioClient(minioProperties.getEndpoint(),
                minioProperties.getAccessKey(), minioProperties.getAccessSecret());

        if (!minioClient.bucketExists(minioProperties.getBucketName())) {
            minioClient.makeBucket(minioProperties.getBucketName());
        }
        minioClient.setBucketPolicy(minioProperties.getBucketName(), "", PolicyType.READ_ONLY);
        return minioClient;
    }
}
