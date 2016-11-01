package com.hanmz.service.dubbo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by hanmz on 2016/8/9.
 */
public class ProviderTest {
  public static void main(String[] args) throws Exception {
    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-dubbo-provider.xml");
    context.start();

    System.in.read(); // 按任意键退出
  }
}
