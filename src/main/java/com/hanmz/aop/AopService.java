package com.hanmz.aop;

import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.concurrent.TimeUnit;

/**
 * Created by hanmz on 2016/11/9.
 */
@Slf4j
@Aspect
public class AopService {
  private AopHandler aopHandler = new AopHandler();

//  @Around(value = "execution(* com.hanmz.service.UserService.*(..))")
  public Object aroundEmailService(ProceedingJoinPoint joinPoint) throws Throwable {
    Stopwatch sw = Stopwatch.createStarted();
    Signature signature = joinPoint.getSignature();
    Object rst;
    try {
      rst = aopHandler.handle(joinPoint);
      return rst;
    } finally {
      long elapsed = sw.stop().elapsed(TimeUnit.MILLISECONDS);
      log.info("ServiceTraceAspect::aroundEmailService, method: {}, elapsed: {}", signature.getName(), elapsed);
    }
  }
}
