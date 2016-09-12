package com.hanmz.service;

import com.hanmz.bean.User;
import com.hanmz.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

  User findById(long id) {
    return userMapper.findById(id);
  }

  void insert(User user) {
    userMapper.insert(user);
  }
}
