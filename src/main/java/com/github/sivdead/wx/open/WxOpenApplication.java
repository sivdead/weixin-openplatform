package com.github.sivdead.wx.open;

import com.github.sivdead.wx.open.config.RedisProperties;
import com.github.sivdead.wx.open.config.WechatOpenProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

/**
 * @author <a href="https://github.com/007gzs">007</a>
 */
@SpringBootApplication
@EnableConfigurationProperties({WechatOpenProperties.class, RedisProperties.class})
@EntityScan(basePackages = "com.github.binarywang.demo.wx.open.dao.eo")
@EnableJpaRepositories
@EnableSpringDataWebSupport
public class WxOpenApplication {
    public static void main(String[] args) {
        SpringApplication.run(WxOpenApplication.class, args);
    }
}
