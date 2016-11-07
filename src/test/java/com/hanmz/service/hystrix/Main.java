package com.hanmz.service.hystrix;

import rx.Observable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by hanmz on 2016/11/3.
 */
public class Main {
  public static void main(String[] args) throws Exception {
    String s = new CommandHelloWorld("han").execute();
    Future<String> fs = new CommandHelloWorld("han").queue();
    System.out.println(s);
    System.out.println(fs.get());
    Observable<String> observer = new ObservableCommandHelloWorld("han").construct();
  }
}
