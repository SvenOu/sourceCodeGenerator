package com.sven.security.dao;

import com.sven.security.vo.UserRole;

import java.util.List;

public interface UserRoleDao {
  int insert(UserRole userRole);

  UserRole findByKey(String key);

  List<UserRole> findListByKey(String key);

  int update(UserRole userRole);

  int updateFields(UserRole userRole, String[] fields);

  int save(UserRole userRole);

  int deleteByKey(String key);
}
