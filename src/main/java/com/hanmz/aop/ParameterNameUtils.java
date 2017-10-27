package com.hanmz.aop;

import com.google.common.collect.ImmutableSet;

import java.lang.reflect.Array;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

/**
 * Created by hanmz on 2016/12/11.
 */
public class ParameterNameUtils {

  private static final Set<String> NAMES = ImmutableSet.of("Boolean", "Character", "Byte", "Short", "Long", "Integer", "Byte", "Float", "Double", "Void", "String");
  private static final TimeZone CHINA_ZONE = TimeZone.getTimeZone("GMT+08:00");
  private static final Locale CHINA_LOCALE = Locale.CHINA;

  public static String getParametersAndValue(Object arg) {
    StringBuilder sbd = new StringBuilder(64);
    getParameters(arg, sbd);
    return sbd.substring(0, sbd.length() - 1);
  }

  /**
   * 递归出全部参数值
   */
  private static String getParameters(Object arg, StringBuilder sbd) {
    if (arg == null) {
      sbd.append("null,");
    } else {
      Class clz = arg.getClass();
      if (isPrimitive(clz)) {
        sbd.append(evalPrimitive(arg)).append(',');
      } else if (clz.isArray()) {
        sbd.append('[');
        for (int i = 0; i < Array.getLength(arg); i++) {
          getParameters(Array.get(arg, i), sbd);
        }
        if (sbd.charAt(sbd.length() - 1) == ',') {
          sbd.insert(sbd.length() - 1, ']');
        } else {
          sbd.append("],");
        }
      } else if (Collection.class.isAssignableFrom(clz)) {
        sbd.append('{');
        ((Collection<?>) arg).forEach(o -> getParameters(o, sbd));
        if (sbd.charAt(sbd.length() - 1) == ',') {
          sbd.insert(sbd.length() - 1, '}');
        } else {
          sbd.append("},");
        }
      } else if (arg instanceof Date) {
        sbd.append('"').append(formatYmdHis(((Date) arg))).append('"');
      } else {
        sbd.append(clz.getSimpleName()).append(":OBJ").append(',');
      }
    }
    return sbd.toString();
  }

  private static boolean isPrimitive(Class clz) {
    return clz.isPrimitive() || NAMES.contains(clz.getSimpleName());
  }

  private static String evalPrimitive(Object obj) {
    String s = String.valueOf(obj);
    if (s.length() > 32) {
      return s.substring(0, 32) + "...";
    }
    return s;
  }

  /**
   * 构造时间的显示，带上时分秒的信息，如 2013-06-11 03:14:25
   *
   * @param date 时间
   * @return 字符串表示
   */
  private static String formatYmdHis(Date date) {
    Calendar ca = Calendar.getInstance(CHINA_ZONE, CHINA_LOCALE);
    ca.setTimeInMillis(date.getTime());
    StringBuilder sbd = new StringBuilder();
    sbd.append(ca.get(Calendar.YEAR)).append('-');
    int month = 1 + ca.get(Calendar.MONTH);
    if (month < 10) {
      sbd.append('0');
    }
    sbd.append(month).append('-');
    int day = ca.get(Calendar.DAY_OF_MONTH);
    if (day < 10) {
      sbd.append('0');
    }
    sbd.append(day).append(' ');
    int hour = ca.get(Calendar.HOUR_OF_DAY);
    if (hour < 10) {
      sbd.append('0');
    }
    sbd.append(hour).append(':');
    int minute = ca.get(Calendar.MINUTE);
    if (minute < 10) {
      sbd.append('0');
    }
    sbd.append(minute).append(':');
    int second = ca.get(Calendar.SECOND);
    if (second < 10) {
      sbd.append('0');
    }
    sbd.append(second);
    return sbd.toString();
  }

  /**
   * 函数参数信息
   */
  public static String getParameters(Object[] args) {
    if (args == null) {
      return "";
    }
    java.lang.StringBuilder sbd = new StringBuilder();
    if (args.length > 0) {
      for (Object i : args) {
        if (i == null) {
          sbd.append("null");
        } else {
          Class clz = i.getClass();
          if (isPrimitive(clz)) {
            sbd.append(evalPrimitive(i));
          } else if (clz.isArray()) {
            sbd.append("arr[").append(Array.getLength(i)).append(']');
          } else if (Collection.class.isAssignableFrom(clz)) {
            sbd.append("col[").append(((Collection<?>) i).size()).append(']');
          } else if (i instanceof Date) {
            sbd.append('"').append(formatYmdHis(((Date) i))).append('"');
          } else {
            sbd.append(clz.getSimpleName()).append(":OBJ");
          }
        }
        sbd.append(',');
      }
      sbd.setLength(sbd.length() - 1);
    }
    return sbd.toString();
  }

}
