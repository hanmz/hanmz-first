package com.hanmz.service;

import com.github.jedis.support.JedisCmd;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by hanmz on 2017/4/8.
 */
@Service
public class RedisService {
  @Resource
  private JedisCmd configJedis;

  public void sadd() {
    configJedis.sadd("han", "test_1");
    configJedis.sadd("han", "test_1");
    configJedis.sadd("han", "test_2");

    Long count = configJedis.srem("han", "test_1");
    Long c = configJedis.srem("zhao", null);

    System.out.println(configJedis.smembers("han").size());
  }

  public void ladd() {
    configJedis.lpush("han", "test_1");
    configJedis.lpush("han", "test_2");
    configJedis.lpush("han", "test_3");

  }
}
