package com.hanmz.service;

import com.alibaba.fastjson.JSON;
import com.hanmz.bean.User;
import com.hanmz.bean.User1;
import com.hanmz.bean.User2;
import org.springframework.stereotype.Service;

/**
 * json 测试
 * Created by hanmz on 2016/8/9.
 */
@Service
public class JsonService {
  public void han() {
    User1 user1 = new User1("han", "han", false);
    String json = JSON.toJSONString(user1);
    User user = JSON.parseObject(json, User.class);
    //    System.out.println(user.getName());
  }

  public void ming() {
    User user = new User("han", "han", false, 1);
    String json = JSON.toJSONString(user);
    User1 user1 = JSON.parseObject(json, User1.class);
    //    System.out.println(user1.getName());
  }

  public void ze() {
    User user = new User("han", "han", false, 1);
    String json = JSON.toJSONString(user);
    User2 user2 = JSON.parseObject(json, User2.class);
    System.out.println(user2.name);
  }
}
