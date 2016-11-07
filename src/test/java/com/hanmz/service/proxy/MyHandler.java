package com.hanmz.service.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by hanmz on 2016/11/2.
 */
public class MyHandler implements InvocationHandler {
  Sub sub = new Sub();

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    String methodName = method.getName();
    switch (methodName) {
      case "method1":
        System.out.println("method1 proxy=" + proxy);
        break;
      case "method2":
        System.out.println(234 + " proxy=" + proxy);
        break;
      default:
        return method.invoke(sub, args);
    }
    return null;
  }
}


class Sub implements Integ {
  public void method2() {
    System.out.println("method2");
  }

  @Override
  public void method3() {
    System.out.println("method3");
  }

  @Override
  public void method4() {
    System.out.println("method4");
  }
}
