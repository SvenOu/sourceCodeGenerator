package com.sven.security.dao;

import com.sven.security.vo.User;

import java.util.List;
import java.util.Map;

public interface UserDao {
  int insert(User user);

  User findByKey(String key);

  List<User> findListByKey(String key);

  int update(User user);

  int updateFields(User user, String[] fields);

  int save(User user);

  int deleteByKey(String key);
}
