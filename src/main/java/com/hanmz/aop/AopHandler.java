package com.hanmz.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Created by hanmz on 2016/11/9.
 */
@Slf4j
public class AopHandler {
  Object handle(ProceedingJoinPoint pjp) {
    log.info("Aop 测试 Signature " + pjp.getSignature().getName());

    try {
      MethodSignature signature = (MethodSignature) pjp.getSignature();
      if (signature.getDeclaringType().isAnnotationPresent(ParamAnno.class) || signature.getMethod().isAnnotationPresent(ParamAnno.class)) {
        System.out.println(ParameterNameUtils.getParametersAndValue(pjp.getArgs()));
      } else {
        System.out.println(ParameterNameUtils.getParameters(pjp.getArgs()));
      }
      return pjp.proceed(pjp.getArgs());
    } catch (Throwable throwable) {
      return null;
    }
  }
}
