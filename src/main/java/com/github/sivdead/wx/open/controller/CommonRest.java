package com.github.sivdead.wx.open.controller;

import cn.hutool.core.io.FileUtil;
import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;
import com.alibaba.fastjson.JSON;
import com.github.sivdead.wx.open.dto.GenerateDatabaseDocRequest;
import com.github.sivdead.wx.open.service.FileService;
import com.github.sivdead.wx.open.web.R;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/common")
@Slf4j
public class CommonRest {


    private final FileService fileService;

    public CommonRest(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/file/policy")
    public R<Map<String, String>> getFilePolicy() {
        return R.ok(fileService.getPolicyFile());
    }

    @PostMapping(value = "/databaseDoc")
    public R<Void> generateDatabaseDoc(@RequestBody GenerateDatabaseDocRequest request, HttpServletResponse response) throws IOException {
        String tempDir = System.getProperty("java.io.tmpdir");
        log.info("tempDir path:{}", tempDir);
        log.info("request:{}", JSON.toJSONString(request));
        EngineConfig engineConfig = EngineConfig.builder()
                .fileName(request.getFilename())
                .produceType(EngineTemplateType.freemarker)
                .fileType(request.getFileType())
                .fileOutputDir(tempDir)
                .build();
        ProcessConfig processConfig = ProcessConfig.builder()
                .ignoreTableName(request.getProcessConfig().getIgnoreTableName())
                .designatedTableName(request.getProcessConfig().getDesignatedTableName())
                .designatedTablePrefix(request.getProcessConfig().getDesignatedTablePrefix())
                .designatedTableSuffix(request.getProcessConfig().getDesignatedTableSuffix())
                .build();
        DataSource dataSource = request.getDatasourceConfig().getDatasource();
        Configuration configuration = Configuration.builder()
                .version(request.getVersion())
                .description(request.getDescription())
                .dataSource(dataSource)
                .engineConfig(engineConfig)
                .produceConfig(processConfig)
                .build();
        new DocumentationExecute(configuration).execute();
        File file = new File(tempDir + File.separator + request.getFilename() + request.getFileType().getFileSuffix());
        FileUtil.writeToStream(file,response.getOutputStream());
        response.getOutputStream().flush();
        response.flushBuffer();
        return R.ok();
    }
}
