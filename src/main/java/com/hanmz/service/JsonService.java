package com.hanmz.service;

import com.github.autoconf.ConfigFactory;
import com.github.autoconf.helper.ConfigHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * json 测试
 * Created by hanmz on 2016/8/9.
 */
@Slf4j
@Service
public class JsonService {
  String ttl = "20s";

  @PostConstruct
  public void init() {
    ConfigFactory.getConfig("hanmz-first", config -> ttl = config.get("ttl", "5s"));
  }

  public void show() {
    System.err.println("ttl = " + ttl);
    System.err.println(ConfigHelper.getProcessInfo().getProfile());
  }
}
