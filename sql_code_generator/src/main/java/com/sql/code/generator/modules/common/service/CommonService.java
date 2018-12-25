package com.sql.code.generator.modules.common.service;

import com.sql.code.generator.modules.common.dataBean.SourceFileInfo;
import net.lingala.zip4j.exception.ZipException;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

/**
 * Created by sven-ou on 2017/2/16.
 */
public interface CommonService {
    String getSourceFileCode(String path) throws IOException;

    SourceFileInfo getCodeFileInfo(String type, String url, String username, String password) throws IOException;

    ResponseEntity<Resource> downloadSourcesFile(String path) throws IOException;

    String generateDirZip(String type) throws IOException, ZipException;
}
