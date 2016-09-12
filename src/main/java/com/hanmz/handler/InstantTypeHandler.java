package com.hanmz.handler;

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
 * Map Java 8 Instant &lt;-&gt; java.sql.Timestamp
 * <p>
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
    Timestamp ts = rs.getTimestamp(columnName);
    if (ts != null) {
      return ts.toInstant();
    }
    return null;
  }

  @Override
  public Instant getResult(ResultSet rs, int columnIndex) throws SQLException {
    Timestamp ts = rs.getTimestamp(columnIndex);
    if (ts != null) {
      return ts.toInstant();
    }
    return null;
  }

  @Override
  public Instant getResult(CallableStatement cs, int columnIndex) throws SQLException {
    Timestamp ts = cs.getTimestamp(columnIndex);
    if (ts != null) {
      return ts.toInstant();
    }
    return null;
  }
}
