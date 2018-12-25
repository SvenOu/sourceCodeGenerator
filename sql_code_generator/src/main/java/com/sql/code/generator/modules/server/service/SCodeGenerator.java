package com.sql.code.generator.modules.server.service;

import java.io.IOException;

/**
 * Created by sven-ou on 2017/2/16.
 */
public interface SCodeGenerator {
    void generateServerCode(String s, String url, String username, String password) throws IOException;
}
