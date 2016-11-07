package com.hanmz.service.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;

/**
 * Created by hanmz on 2016/11/3.
 */
public class CommandHelloWorld extends HystrixCommand<String> {

  private final String name;

  public CommandHelloWorld(String name) {
    super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("HelloWorld")));
    HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(1000);
    this.name = name;
  }

  @Override
  protected String run() {
    if ("han".equals(name)) {
      int[] arr = new int[1];
      System.out.println(arr[1]);
    }
    return "Hello " + name + "!";
  }

  @Override
  protected String getFallback() {
    return "han";
  }

}
