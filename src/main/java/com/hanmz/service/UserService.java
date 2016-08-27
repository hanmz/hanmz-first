package com.hanmz.service;

import com.hanmz.bean.User;
import com.hanmz.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by hanmz on 2016/8/4.
 */
@Service
public class UserService {
  @Autowired
  UserMapper userMapper;

  public void findById(long id) {
    User user = userMapper.findById(id);
    System.out.println(user.isAdmin());
  }

  public void insert() {
    userMapper.insert(new User("han", "han", false, 0));
  }
}
