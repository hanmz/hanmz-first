package com.hanmz.interceptor;

import com.github.mybatis.reporter.RecordReporter;
import com.github.trace.TraceContext;
import com.hanmz.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;

import java.lang.reflect.Field;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * *
 * Created by hanmz on 2017/12/11.
 *
 * @author hanmz
 */
@Slf4j
@Intercepts({@Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}), @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}), @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class})})
public class SqlCostInterceptor implements Interceptor {

  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    Object target = invocation.getTarget();

    long startTime = System.currentTimeMillis();
    StatementHandler statementHandler = (StatementHandler) target;

    Statement statement = (Statement) invocation.getArgs()[0];
    //    Field h =  ((Proxy)statement).getClass().getDeclaredField("h");

    try {
      return invocation.proceed();
    } finally {
      // 查询耗时
      long sqlCost = System.currentTimeMillis() - startTime;

      if (RecordReporter.needReport(sqlCost)) {
        BoundSql boundSql = statementHandler.getBoundSql();
        String sql = boundSql.getSql();
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappings();

        // 格式化Sql语句，替换参数
        sql = formatSql(sql, parameterObject, parameterMappingList);

        // TODO: hanmz 2017/12/6 记录执行耗时长的sql语句
        TraceContext traceContext = TraceContext.get().setCost(sqlCost);
        RecordReporter.getInstance().sendLog(traceContext, sql);

        log.debug("SQL:[" + sql + "] 执行耗时:[" + sqlCost + "ms]");
      }
    }
  }

  @Override
  public Object plugin(Object target) {
    if (target instanceof StatementHandler) {
      return Plugin.wrap(target, this);
    } else {
      return target;
    }
  }

  @Override
  public void setProperties(Properties properties) {

  }

  @SuppressWarnings("unchecked")
  private String formatSql(String sql, Object parameterObject, List<ParameterMapping> parameterMappingList) {
    if (sql == null || sql.length() == 0) {
      return "";
    }
    sql = beautifySql(sql);
    if (parameterObject == null || parameterMappingList == null || parameterMappingList.size() == 0) {
      return sql;
    }
    // 定义一个没有替换过占位符的sql，用于出异常时返回
    String sqlWithoutReplacePlaceholder = sql;
    try {
      Class<?> parameterObjectClass = parameterObject.getClass();
      // list
      if (isList(parameterObjectClass)) {
        return handleListParameter(sql, (Collection<?>) parameterObject);
      }
      // map
      if (isMap(parameterObjectClass)) {
        return handleMapParameter(sql, parameterMappingList, (Map<?, ?>) parameterObject);
      }
      // object
      return handleCommon(sql, parameterMappingList, parameterObjectClass, parameterObject);
    } catch (Exception e) {
      e.printStackTrace();
      return sqlWithoutReplacePlaceholder;
    }
  }

  /**
   * 处理参数为List的场景
   */
  private String handleListParameter(String sql, Collection<?> col) {
    if (col != null && col.size() != 0) {
      for (Object obj : col) {
        String value = "";
        Class<?> objClass = obj.getClass();
        if (isPrimitiveOrPrimitiveWrapper(objClass)) {
          value = obj.toString();
        } else if (objClass.isAssignableFrom(String.class)) {
          value = "\"" + obj.toString() + "\"";
        }

        sql = sql.replaceFirst("\\?", value);
      }
    }
    return sql;
  }

  /**
   * 处理参数为Map场景
   */
  private String handleMapParameter(String sql, List<ParameterMapping> parameterMappingList, Map<?, ?> parameterObject) throws Exception {
    for (ParameterMapping parameterMapping : parameterMappingList) {
      Object value = parameterObject.get(parameterMapping.getProperty());

      String propertyValue = value != null ? value.toString() : "缺失";
      sql = sql.replaceFirst("\\?", propertyValue);
    }

    return sql;
  }

  /**
   * 处理通用场景
   */
  private String handleCommon(String sql, List<ParameterMapping> parameterMappingList, Class<?> parameterObjectClass, Object parameterObject) throws Exception {

    for (ParameterMapping parameterMapping : parameterMappingList) {
      String propertyValue;
      // 基本数据类型或者基本数据类型的包装类，直接toString即可获取其真正的参数值，其余直接取parameterMapping中的property属性即可
      if (isPrimitiveOrPrimitiveWrapper(parameterObjectClass)) {
        propertyValue = parameterObject.toString();
      } else {
        String propertyName = parameterMapping.getProperty();
        Field field = getField(parameterObjectClass, propertyName);
        if (field == null) {
          propertyValue = "缺失";
        } else {
          field.setAccessible(true);
          propertyValue = String.valueOf(field.get(parameterObject));
          if (parameterMapping.getJavaType().isAssignableFrom(String.class)) {
            propertyValue = "\"" + propertyValue + "\"";
          }
        }
      }
      sql = sql.replaceFirst("\\?", propertyValue);
    }

    return sql;
  }

  /**
   * 一直遍历到最上层
   */
  private static Field getField(Class clazz, String propertyName) {
    while (clazz != null && !clazz.getName().toLowerCase().equals(Object.class.getTypeName())) {
      try {
        return clazz.getDeclaredField(propertyName);
      } catch (Exception e) {
        clazz = clazz.getSuperclass();
      }
    }
    return null;
  }

  /**
   * 是否基本数据类型或者基本数据类型的包装类
   */
  private boolean isPrimitiveOrPrimitiveWrapper(Class<?> parameterObjectClass) {
    return parameterObjectClass.isPrimitive() || Number.class.isAssignableFrom(parameterObjectClass) ||
      Character.class.isAssignableFrom(parameterObjectClass) || Boolean.class.isAssignableFrom(parameterObjectClass);
  }

  /**
   * 是否HashMap
   */
  private boolean isMap(Class<?> clazz) {
    return Map.class.isAssignableFrom(clazz);
  }

  /**
   * 是否List的实现类
   */
  private boolean isList(Class<?> clazz) {
    return List.class.isAssignableFrom(clazz);
  }

  /**
   * 美化Sql 去掉多余空白字符
   */
  private String beautifySql(String sql) {
    sql = sql.replaceAll("\\s+", " ").replace("( ", "(").replace(" )", ")").replace(" ,", ",");
    return sql;
  }

  public static void main(String[] args) {
    //    Integer i = 10;
    //    Class<?> clazz = i.getClass();
    //    System.out.println(Number.class.isAssignableFrom(clazz));
    //    System.out.println(clazz.isAssignableFrom(Number.class));


    System.out.println(getField(User.class, "id"));

    System.out.println(Object.class.getTypeName());
  }
}
