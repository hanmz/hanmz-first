package com.hanmz.service;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Created by hanmz on 2016/11/9.
 */
public class AopHandler {
  Object handle(ProceedingJoinPoint pjp) {
    try {
      Object ret = pjp.proceed(pjp.getArgs());
      return ret;
    } catch (Throwable throwable) {
      return null;
    }
  }
}
