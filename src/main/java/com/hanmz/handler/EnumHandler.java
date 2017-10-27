//package com.hanmz.handler;
//
//import com.alibaba.fastjson.JSON;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.ibatis.type.BaseTypeHandler;
//import org.apache.ibatis.type.JdbcType;
//
//import java.io.IOException;
//import java.sql.CallableStatement;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
///**
// * 通过实现JsonHandler接口方式
// * <p>
// * Created by hanmz on 2016/9/12.
// */
//public class EnumHandler<T> extends BaseTypeHandler<T> {
//  private final ObjectMapper mapper = new ObjectMapper();
//  private final Class<T> javaType;
//
//  public EnumHandler(Class<T> javaType) {
//    this.javaType = javaType;
//  }
//
//  @Override
//  public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
//    try {
//      ps.setString(i, Enum.mapper.writeValueAsString(JSON.toJSONString(parameter)));
//    } catch (JsonProcessingException e) {
//      throw new SQLException(e);
//    }
//  }
//
//  @Override
//  public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
//    return toJavaTypeObject(rs.getObject(columnName));
//  }
//
//  @Override
//  public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
//    return toJavaTypeObject(rs.getObject(columnIndex));
//  }
//
//  @Override
//  public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
//    return toJavaTypeObject(cs.getObject(columnIndex));
//  }
//
//  private T toJavaTypeObject(Object value) throws SQLException {
//    if (value == null) {
//      return null;
//    }
//
//    try {
//      String s = mapper.readValue(value.toString(), String.class);
//      return JSON.parseObject(s, javaType);
//    } catch (IOException e) {
//      throw new SQLException(e);
//    }
//  }
//}