package com.hanmz.service.hystrix;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by hanmz on 2016/11/3.
 */
public class ObservableCommandHelloWorld extends HystrixObservableCommand<String> {

  private final String name;

  public ObservableCommandHelloWorld(String name) {
    super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
    this.name = name;
  }

  @Override
  protected Observable<String> construct() {
    return Observable.create(new Observable.OnSubscribe<String>() {
      @Override
      public void call(Subscriber<? super String> observer) {
        try {
          if (!observer.isUnsubscribed()) {
            // a real example would do work like a network call here
            observer.onNext("Hello");
            observer.onNext(name + "!");
            observer.onCompleted();
          }
        } catch (Exception e) {
          observer.onError(e);
        }
      }
    } );
  }
}
