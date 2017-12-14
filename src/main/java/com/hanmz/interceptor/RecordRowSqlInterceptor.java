package com.hanmz.interceptor;

import com.github.autoconf.intern.InnerUtils;
import com.github.mybatis.reporter.RecordReporter;
import com.github.trace.TraceContext;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Matcher;

import static com.github.mybatis.provider.CrudProvider.FIELD_LEFT;
import static com.github.mybatis.provider.CrudProvider.FIELD_RIGHT;

/**
 * *
 * Created by hanmz on 2017/12/10.
 *
 * @author hanmz
 */
@Slf4j
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}), @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
@SuppressWarnings({"unchecked", "rawtypes"})
public class RecordRowSqlInterceptor implements Interceptor {
  private String dialect;

  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    long start = System.currentTimeMillis();
    MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
    Object parameter = invocation.getArgs().length > 1 ? invocation.getArgs()[1] : null;

    try {
      return invocation.proceed();
    } finally {
      long cost = System.currentTimeMillis() - start;
      if (RecordReporter.needReport(cost)) {
        // TODO: hanmz 2017/12/6 记录执行耗时长的sql语句
        TraceContext traceContext = TraceContext.get().setCost(cost);
        recordSql(mappedStatement, parameter, traceContext);
      }
    }
  }

  /**
   * 记录耗时长的sql
   */
  private void recordSql(MappedStatement mappedStatement, Object parameter, TraceContext traceContext) {
    try {
      // 获取sql语句id
      String sqlId = mappedStatement.getId();
      // BoundSql就是封装myBatis最终产生的sql类
      BoundSql boundSql = mappedStatement.getBoundSql(parameter);
      // 获取节点的配置
      Configuration configuration = mappedStatement.getConfiguration();
      // 获取到最终的sql语句
      String sql = getSql(configuration, boundSql, sqlId);

      log.debug(sql);

      // TODO: hanmz 2017/12/6 记录执行耗时长的sql语句
      RecordReporter.getInstance().sendLog(traceContext, sql);
    } catch (Exception e) {
      log.warn(e.getMessage(), e);
    }
  }

  /**
   * 封装了一下sql语句，使得结果返回完整xml路径下的sql语句节点id + sql语句
   */
  private String getSql(Configuration configuration, BoundSql boundSql, String sqlId) {
    String sql = showSql(configuration, boundSql);
    return sqlId + ":" + replaceSeparator(sql);
  }

  /**
   * 根据不同类型语言替换sql语句中的不可见字符
   */
  private String replaceSeparator(String sql) {
    if (sql.indexOf(FIELD_LEFT) == -1 || sql.indexOf(FIELD_RIGHT) == -1) {
      return sql;
    }
    final char left;
    final char right;
    if ("postgresql".equals(dialect)) {
      left = right = '"';
    } else if ("sqlserver".equals(dialect)) {
      left = '[';
      right = ']';
    } else {
      left = right = '`';
    }
    StringBuilder sbd = new StringBuilder(sql.length());
    for (char ch : sql.toCharArray()) {
      if (FIELD_LEFT == ch) {
        sbd.append(left);
      } else if (FIELD_RIGHT == ch) {
        sbd.append(right);
      } else {
        sbd.append(ch);
      }
    }

    return sbd.toString();
  }

  /**
   * 如果参数是String，则添加单引号， 如果是日期，则转换为时间格式器并加单引号； 对参数是null和不是null的情况作了处理<br>
   */
  private String getParameterValue(Object obj) {
    String value;
    if (obj instanceof String) {
      value = "'" + obj.toString() + "'";
    } else if (obj instanceof Date) {
      DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
      value = "'" + formatter.format(new Date()) + "'";
    } else {
      if (obj != null) {
        value = obj.toString();
      } else {
        value = "";
      }

    }
    return value;
  }

  /**
   * 进行？的替换
   */
  private String showSql(Configuration configuration, BoundSql boundSql) {
    // 获取参数
    Object parameterObject = boundSql.getParameterObject();
    List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
    // sql语句中多个空格都用一个空格代替
    String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
    if (!InnerUtils.isNullOrEmpty(parameterMappings) && parameterObject != null) {
      // 获取类型处理器注册器，类型处理器的功能是进行java类型和数据库类型的转换　　　　　
      // 如果根据parameterObject.getClass(）可以找到对应的类型，则替换
      TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
      if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
        sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(parameterObject)));

      } else {
        MetaObject metaObject = configuration.newMetaObject(parameterObject);
        // MetaObject主要是封装了originalObject对象，提供了get和set的方法用于获取和设置originalObject的属性值,主要支持对JavaBean、Collection、Map三种类型对象的操作
        for (ParameterMapping parameterMapping : parameterMappings) {
          String propertyName = parameterMapping.getProperty();
          if (metaObject.hasGetter(propertyName)) {
            Object obj = metaObject.getValue(propertyName);
            sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
          } else if (boundSql.hasAdditionalParameter(propertyName)) {
            // 该分支是动态sql
            Object obj = boundSql.getAdditionalParameter(propertyName);
            sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
          } else {
            //打印出缺失，提醒该参数缺失并防止错位
            sql = sql.replaceFirst("\\?", "缺失");
          }
        }
      }
    }
    return sql;
  }

  @Override
  public void setProperties(Properties properties) {
    String val = properties.getProperty("dialect");
    if (Strings.isNullOrEmpty(val)) {
      dialect = "mysql";
      return;
    }
    val = val.toLowerCase();
    if (val.startsWith("mysql")) {
      dialect = "mysql";
    } else if (val.startsWith("sqlserver")) {
      dialect = "sqlserver";
    } else if (val.startsWith("postgres")) {
      dialect = "postgresql";
    } else if (val.startsWith("oracle")) {
      dialect = "oracle";
    } else if (val.startsWith("hsqldb")) {
      dialect = "hsqldb";
    } else if (val.startsWith("h2")) {
      dialect = "h2";
    } else {
      dialect = "mysql";
    }
  }

  @Override
  public Object plugin(Object target) {
    if (target instanceof Executor) {
      return Plugin.wrap(target, this);
    } else {
      return target;
    }
  }

}
