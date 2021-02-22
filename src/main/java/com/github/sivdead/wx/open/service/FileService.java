package com.github.sivdead.wx.open.service;

import cn.hutool.core.io.FileUtil;
import com.github.sivdead.wx.open.config.MinioProperties;
import com.github.sivdead.wx.open.exception.BusinessException;
import io.minio.MinioClient;
import io.minio.PostPolicy;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.Map;

@Service
@Slf4j
public class FileService {


    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    public FileService(MinioClient minioClient, MinioProperties MinioProperties) {
        this.minioClient = minioClient;
        this.minioProperties = MinioProperties;
    }

    public Map<String, String> getPolicyFile() {
        try {
            DateTime dateTime = DateTime.now().plusMinutes(30);
            PostPolicy postPolicy = new PostPolicy(minioProperties.getBucketName(), "", true, dateTime);
            return minioClient.presignedPostPolicy(postPolicy);
        } catch (Exception e) {
            log.error("获取minio policy失败", e);
            throw new BusinessException(e.getMessage());
        }
    }


}
