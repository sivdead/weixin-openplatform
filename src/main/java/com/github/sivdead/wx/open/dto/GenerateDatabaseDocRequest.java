package com.github.sivdead.wx.open.dto;

import cn.smallbun.screw.core.engine.EngineFileType;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zaxxer.hikari.HikariDataSource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerateDatabaseDocRequest {

    private ProcessConfig processConfig;

    private DatasourceConfig datasourceConfig;

    private String version;

    private String description;

    private String filename;

    private EngineFileType fileType;


    /**
     * 数据处理
     *
     * @author SanLi
     * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/4/1 22:22
     */
    @Data
    @Builder
    @AllArgsConstructor
    public static class ProcessConfig implements Serializable {
        /**
         * 忽略表名
         */
        private List<String> ignoreTableName;
        /**
         * 忽略表前缀
         */
        private List<String> ignoreTablePrefix;
        /**
         * 忽略表后缀
         */
        private List<String> ignoreTableSuffix;
        /**
         * 指定生成表名
         *
         * @see "1.0.3"
         */
        private List<String> designatedTableName;
        /**
         * 指定生成表前缀
         *
         * @see "1.0.3"
         */
        private List<String> designatedTablePrefix;
        /**
         * 指定生成表后缀
         *
         * @see "1.0.3"
         */
        private List<String> designatedTableSuffix;
    }


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DatasourceConfig implements Serializable {
        private String driverClassName;
        private String url;
        private String username;
        private String password;

        @JsonIgnore
        @JSONField(serialize = false)
        public DataSource getDatasource(){
            return DataSourceBuilder.create()
                    .type(HikariDataSource.class)
                    .driverClassName(driverClassName)
                    .url(url)
                    .username(username)
                    .password(password)
                    .build();
        }
    }

}
