package com.hanmz.service.proxy;

import com.google.common.reflect.Reflection;

/**
 * Created by hanmz on 2016/11/2.
 */
public class Main {
  public static void main(String[] args) {
    Subject proxy = Reflection.newProxy(Subject.class, new MyHandler());
    proxy.method1();
    proxy.method2();
    proxy.method3();
    proxy.method4();
  }
}
