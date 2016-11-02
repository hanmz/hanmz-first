package com.hanmz.service;

import com.github.jedis.support.JedisCmd;
import com.hanmz.bean.User;
import com.hanmz.mapper.UserMapper;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Created by hanmz on 2016/8/4.
 */
@Service
public class UserService {
  @Autowired
  UserMapper userMapper;
  @Resource
  private JedisCmd jedis;

  public void set(String key, String value) {
    jedis.set(key, value);
  }

  public String get(String key) {
    return jedis.get(key);
  }

  public Long del(String... key) {
    return jedis.del(key);
  }

  User findById(long id) {
    return userMapper.findById(id);
  }

  User findByMessage(String message) {
    return userMapper.findByMessage(message);
  }

  void insert(User user) {
    int i = userMapper.insert(user);
    long j = user.getId();
    System.out.println(i);
  }
}
