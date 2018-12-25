package com.sql.code.generator.modules.android.service;

import java.io.IOException;

/**
 * Created by sven-ou on 2017/2/16.
 */
public interface CodeGenerator {
    void generateAndroidCode(String s, String url, String username, String password) throws IOException;
}
