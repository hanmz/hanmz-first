package com.hanmz.handler;

import com.alibaba.fastjson.JSON;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 通过实现JsonHandler接口方式
 * <p>
 * Created by hanmz on 2016/9/12.
 */
public class ListHandler<T> extends BaseTypeHandler<T> {
  private final Class<T> javaType;

  public ListHandler(Class<T> javaType) {
    this.javaType = javaType;
  }

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
    ps.setString(i, JSON.toJSONString(parameter));
  }

  @Override
  public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return toJavaTypeObject(rs.getObject(columnName));
  }

  @Override
  public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return toJavaTypeObject(rs.getObject(columnIndex));
  }

  @Override
  public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    return toJavaTypeObject(cs.getObject(columnIndex));
  }

  private T toJavaTypeObject(Object value) throws SQLException {
    if (value == null) {
      return null;
    }

    return JSON.parseObject(value.toString(), javaType);
  }
}