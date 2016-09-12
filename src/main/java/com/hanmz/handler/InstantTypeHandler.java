package com.hanmz.handler;

import com.github.mybatis.util.UnicodeFormatter;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * Created by hanmz on 2016/9/12.
 */
@MappedTypes(Instant.class)
public class InstantTypeHandler implements TypeHandler<Instant> {
  @Override
  public void setParameter(PreparedStatement ps, int i, Instant parameter, JdbcType jdbcType) throws SQLException {
    ps.setTimestamp(i, Timestamp.from(parameter));
  }

  @Override
  public Instant getResult(ResultSet rs, String columnName) throws SQLException {
    return rs.getTimestamp(columnName).toInstant();
  }

  @Override
  public Instant getResult(ResultSet rs, int columnIndex) throws SQLException {
    return rs.getTimestamp(columnIndex).toInstant();
  }

  @Override
  public Instant getResult(CallableStatement cs, int columnIndex) throws SQLException {
    return cs.getTimestamp(columnIndex).toInstant();
  }
}
