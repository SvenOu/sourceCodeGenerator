package com.sql.code.generator.modules.common.service;

import com.sven.common.lib.bean.CommonResponse;
import com.sven.common.lib.codetemplate.dataBean.SourceFileInfo;
import net.lingala.zip4j.exception.ZipException;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

/**
 * Created by sven-ou on 2017/2/16.
 */
public interface CommonService {
    String getSourceFileCode(String path) throws IOException;

    SourceFileInfo getCodeFileInfo(String packageName,
                                   String dataSourceId,
                                   String templateId) throws IOException;

    ResponseEntity<Resource> downloadSourcesFile(String path) throws IOException;

    String generateDirZip(String dataSourceId, String templateId) throws IOException, ZipException;

    String generateUserDirZip() throws ZipException;

    String getDoucumentFile() throws IOException;

    SourceFileInfo getUserGenerateRootCodeFileInfo();

    CommonResponse clearGenerateCode() throws IOException;

    String downloadAllTemplateFile() throws IOException, ZipException;
}
