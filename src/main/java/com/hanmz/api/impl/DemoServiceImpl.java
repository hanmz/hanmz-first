package com.hanmz.api.impl;

import com.hanmz.api.DemoService;

/**
 * Created by hanmz on 2016/10/21.
 */
public class DemoServiceImpl implements DemoService {

  public String sayHello(String name) {
    return "Hello " + name;
  }

}
