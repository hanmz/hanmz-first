package com.hanmz.service.mongo;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by hanmz on 2016/11/8.
 */
public class ReflectionUtil {
  public static Type[] getParameterizedTypes(Object object) {
    Type superclassType = object.getClass().getGenericSuperclass();
    if (!ParameterizedType.class.isAssignableFrom(superclassType.getClass())) {
      return null;
    }
    return ((ParameterizedType)superclassType).getActualTypeArguments();
  }

}
