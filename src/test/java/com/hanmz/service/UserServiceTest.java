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
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.core.IsEqual.equalTo;

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
    userService.set("han1", "hh");
  }
}
