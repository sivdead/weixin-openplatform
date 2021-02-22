package com.github.sivdead.wx.open;

import com.github.sivdead.wx.open.config.WechatOpenProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author <a href="https://github.com/007gzs">007</a>
 */
@SpringBootApplication
@EntityScan(basePackages = "com.github.sivdead.wx.open.dao.eo")
@EnableJpaRepositories
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
