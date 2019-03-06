package com.sql.code.generator.modules.common.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface MobileService {
    <T> String getForEntity(String url, Class<T> responseType, Object... uriVariables);

    String saveFileToTemp(MultipartFile file, String targetPath) throws IOException;

    boolean uploadOverwriteFile(String path, String targetPath, String mobileActionUrl) throws IOException;
}
