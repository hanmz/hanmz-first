package com.hanmz.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Created by hanmz on 2016/11/9.
 */
@Slf4j
public class AopHandler {
  Object handle(ProceedingJoinPoint pjp) {
    log.info("Aop 测试 Signature " + pjp.getSignature().getName());
    try {
      Object ret = pjp.proceed(pjp.getArgs());
      return ret;
    } catch (Throwable throwable) {
      return null;
    }
  }
}
