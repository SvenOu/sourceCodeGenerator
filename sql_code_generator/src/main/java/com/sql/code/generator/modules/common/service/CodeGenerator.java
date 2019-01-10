package com.sql.code.generator.modules.common.service;

import java.io.IOException;
import java.util.Map;

public interface CodeGenerator {
    Map generateCodeModel(String packageName, String driverClassName, String url, String username, String password);
    void generateCodeFiles(Map rootContext, String tplDirPath, String disPath) throws IOException;
}
