package com.github.sivdead.wx.open.service;

import com.github.sivdead.wx.open.config.RedisProperties;
import com.github.sivdead.wx.open.config.WechatOpenProperties;
import me.chanjar.weixin.open.api.impl.WxOpenInRedisConfigStorage;
import me.chanjar.weixin.open.api.impl.WxOpenMessageRouter;
import me.chanjar.weixin.open.api.impl.WxOpenServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;

/**
 * @author <a href="https://github.com/007gzs">007</a>
 */
@Service
public class WxOpenService extends WxOpenServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(WxOpenService.class);
    private final WechatOpenProperties wechatOpenProperties;
    private final RedisProperties redisProperties;
    private volatile static JedisPool pool;
    private WxOpenMessageRouter wxOpenMessageRouter;

    public WxOpenService(WechatOpenProperties wechatOpenProperties, RedisProperties redisProperties) {
        this.wechatOpenProperties = wechatOpenProperties;
        this.redisProperties = redisProperties;
    }

    @PostConstruct
    public void init() {
        WxOpenInRedisConfigStorage inRedisConfigStorage = new WxOpenInRedisConfigStorage(getJedisPool());
        inRedisConfigStorage.setComponentAppId(wechatOpenProperties.getComponentAppId());
        inRedisConfigStorage.setComponentAppSecret(wechatOpenProperties.getComponentSecret());
        inRedisConfigStorage.setComponentToken(wechatOpenProperties.getComponentToken());
        inRedisConfigStorage.setComponentAesKey(wechatOpenProperties.getComponentAesKey());
        setWxOpenConfigStorage(inRedisConfigStorage);
        wxOpenMessageRouter = new WxOpenMessageRouter(this);
        wxOpenMessageRouter.rule().handler((wxMpXmlMessage, map, wxMpService, wxSessionManager) -> {
            logger.info("\n接收到 {} 公众号请求消息，内容：{}", wxMpService.getWxMpConfigStorage().getAppId(), wxMpXmlMessage);
            return null;
        }).next();
    }
    public WxOpenMessageRouter getWxOpenMessageRouter(){
        return wxOpenMessageRouter;
    }

    private JedisPool getJedisPool() {
        if (pool == null) {
            synchronized (WxOpenService.class) {
                if (pool == null) {
                    pool = new JedisPool(redisProperties, redisProperties.getHost(),
                            redisProperties.getPort(), redisProperties.getConnectionTimeout(),
                            redisProperties.getSoTimeout(), redisProperties.getPassword(),
                            redisProperties.getDatabase(), redisProperties.getClientName(),
                            redisProperties.isSsl(), redisProperties.getSslSocketFactory(),
                            redisProperties.getSslParameters(), redisProperties.getHostnameVerifier());
                }
            }
        }
        return pool;
    }
}
