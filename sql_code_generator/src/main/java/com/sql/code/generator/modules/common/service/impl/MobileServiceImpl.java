package com.sql.code.generator.modules.common.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sql.code.generator.modules.common.service.FileService;
import com.sql.code.generator.modules.common.service.MobileService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author sven-ou
 */
@Service
public class MobileServiceImpl implements MobileService {
    private static Log log = LogFactory.getLog(MobileServiceImpl.class);

    @Autowired
    private FileService fileService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String TEMP_FILE_NAME = "z_tempOverwriteFiles";

    @Override
    public <T> String getForEntity(String url, Class<T> responseType, Object... uriVariables)
            throws RestClientException {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url,  String.class, uriVariables);
        return responseEntity.getBody();
    }

    @Override
    public String saveFileToTemp(MultipartFile file, String targetPath) throws IOException {
        String savePath = fileService.getUserGeneratorDirPath() + "/" + TEMP_FILE_NAME + "/" + file.getOriginalFilename();
        File newFile = new File(savePath);
        File newFileParent = newFile.getParentFile();
        if(!newFile.exists()){
            newFileParent.mkdirs();
        }
        FileCopyUtils.copy(file.getBytes(), newFile);
        return savePath;
    }

    @Override
    public boolean uploadOverwriteFile(String path, String targetPath, String mobileActionUrl) throws IOException {
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("file", new FileSystemResource(path));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
        ResponseEntity<String> result = restTemplate.exchange(
                mobileActionUrl, HttpMethod.POST, requestEntity,
                String.class);
        FileSystemUtils.deleteRecursively(Paths.get(fileService.getUserGeneratorDirPath() + "/" + TEMP_FILE_NAME + "/"));
        return "success".equals(result.getBody());
    }
}
