package com.sql.code.generator.modules.common.dao;

import com.sql.code.generator.modules.common.vo.CodeTemplate;

import java.util.List;

public interface CodeTemplateDao {
  int insert(CodeTemplate codeTemplate);

  CodeTemplate findByKey(String templateId);

  List<CodeTemplate> findListByKey(String templateId);

  int update(CodeTemplate codeTemplate);

  int updateFields(CodeTemplate codeTemplate, String[] fields);

  int save(CodeTemplate codeTemplate);

  int deleteByKey(String templateId);

  List<CodeTemplate> findAll();
}
