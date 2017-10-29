package com.hanmz.mapper;

import com.github.mybatis.util.EntityUtil;
import com.github.mybatis.util.PersistMeta;
import org.apache.ibatis.jdbc.AbstractSQL;
import org.apache.ibatis.jdbc.SQL;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.regex.Pattern;

import static com.github.mybatis.util.EntityUtil.nameConvert;

/**
 * 根据多个字段查询、删除
 */
public class MyProvider {
  /**
   * 按指定字段更新
   */
  public String update(final Object obj, String updateStatement, String... fields) {
    final PersistMeta meta = EntityUtil.getMeta(obj.getClass());
    return new SQL() {
      {
        UPDATE(getTableName(meta, obj)).SET(updateStatement);
        setWhere(this, obj, fields);
      }
    }.toString();
  }

  /**
   * 按指定字段查询
   */
  public String select(final Object obj, String... fields) {
    final PersistMeta meta = EntityUtil.getMeta(obj.getClass());
    return new SQL() {
      {
        SELECT("*").FROM(getTableName(meta, obj));
        setWhere(this, obj, fields);
      }
    }.toString();
  }

  /**
   * 按指定字段名删除
   */
  public String delete(final Object obj, String... fields) {
    final PersistMeta meta = EntityUtil.getMeta(obj.getClass());
    return new SQL() {
      {
        DELETE_FROM(getTableName(meta, obj));
        setWhere(this, obj, fields);
      }
    }.toString();
  }

  private void setWhere(AbstractSQL sql, final Object obj, String... fields) {
    if (fields == null || fields.length == 0) {
      String val = getField(obj, "id");
      sql.WHERE("id='" + val + "'");
    } else {
      StringBuilder sb = new StringBuilder(64);
      for (String field : fields) {
        String val = getField(obj, field);
        checkSql(field, val);
        sb.append(nameConvert(field, true)).append("='").append(val).append("'").append(",");
      }
      sql.WHERE(sb.substring(0, sb.length() - 1));
    }
  }

  /**
   * 检查sql注入
   */
  private void checkSql(String field, String targetStr) {
    final String checkSql = "(.+)\\sand\\s(.+)|(.+)\\sor(.+)";
    if (Pattern.matches(checkSql, targetStr)) {
      throw new RuntimeException(String.format("参数可能存在sql注入，参数：%s，值：%s", field, targetStr));
    }
  }

  /**
   * 获取field的值
   */
  private String getField(Object obj, String fieldName) {
    try {
      Class<?> clazz = obj.getClass();
      Field field = clazz.getDeclaredField(fieldName);
      String typeName = field.getGenericType().toString();

      Method method;
      if (typeName.equals("boolean") || typeName.equals("java.lang.Boolean")) {
        method = clazz.getMethod("is" + getMethodName(fieldName));
      } else {
        method = clazz.getMethod("get" + getMethodName(fieldName));
      }

      Object res = method.invoke(obj);
      if (res instanceof Date) {
        return Long.valueOf(((Date) res).getTime()).toString();
      }

      return method.invoke(obj).toString();
    } catch (Exception e) {
      throw new RuntimeException("");
    }
  }

  private String getMethodName(String fieldName) {
    byte[] items = fieldName.getBytes();
    items[0] = (byte) ((char) items[0] - 'a' + 'A');
    return new String(items);
  }

  private String getTableName(PersistMeta meta, Object obj) {
    if (meta.getPostfix() != null) {
      try {
        return meta.getTableName() + '_' + meta.getPostfix().invoke(obj);
      } catch (Exception ignored) {
      }
    }
    return meta.getTableName();
  }
}
