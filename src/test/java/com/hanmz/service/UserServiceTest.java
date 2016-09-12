package com.hanmz.service;

import com.hanmz.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

/**
 * Created by hanmz on 2016/8/4.
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class UserServiceTest {
  @Autowired
  UserService userService;

  @Test
  public void findById() throws Exception {
    User user = userService.findById(2);
    System.err.println("create_time = " + user.getCreateTime());
  }

  @Test
  public void insert() {
    userService.insert();
  }

  public static void main(String[] args) {
    System.out.println(LocalDate.now());
  }

}
