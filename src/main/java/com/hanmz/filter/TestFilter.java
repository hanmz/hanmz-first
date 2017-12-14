package com.hanmz.filter;

import com.alibaba.druid.filter.FilterEventAdapter;
import com.alibaba.druid.proxy.jdbc.JdbcParameter;
import com.alibaba.druid.proxy.jdbc.StatementProxy;
import com.github.mybatis.reporter.RecordReporter;
import com.github.trace.TraceContext;
import lombok.extern.slf4j.Slf4j;

import java.sql.Types;

/**
 * 自定义druid filter 方便统计慢查询
 * Created by hanmz on 2017/12/14.
 *
 * @author hanmz
 */
@Slf4j
public class TestFilter extends FilterEventAdapter {
  private static final char SPACE = ' ';

  @Override
  protected void statementExecuteBefore(StatementProxy statement, String sql) {

    super.statementExecuteBefore(statement, sql);
  }

  @Override
  protected void statementExecuteAfter(StatementProxy statement, String sql, boolean result) {

    try {
      // 纳秒转毫秒
      long cost = statement.getLastExecuteTimeNano() / 10000000;
      if (!RecordReporter.needReport(cost)) {
        return;
      }

      // 替换占位符?
      char[] chars = sql.toCharArray();
      StringBuilder sbd = new StringBuilder(sql.length() * 2);
      int i = 0;
      int parametersSize = statement.getParametersSize();
      for (char c : chars) {
        if ('?' == c && i < parametersSize) {
          JdbcParameter parameter = statement.getParameter(i++);
          if (parameter == null) {
            continue;
          }
          int sqlType = parameter.getSqlType();
          Object value = parameter.getValue();
          switch (sqlType) {
            case Types.NULL:
              sbd.append("NULL");
              break;
            default:
              sbd.append(String.valueOf(value));
              break;
          }
        } else if ('\n' == c || '\t' == c) {
          append(sbd, SPACE);
        } else {
          append(sbd, c);
        }
      }

      // 移除多余的空格
      String rawSql = sbd.toString();

      log.debug(rawSql);

      // 上报慢查询
      RecordReporter.getInstance().sendLog(TraceContext.get(), rawSql);
    } catch (Exception e) {
      log.debug(e.getMessage());
    }

    super.statementExecuteAfter(statement, sql, result);
  }

  private void append(StringBuilder sbd, char c) {
    if (c == SPACE && sbd.length() > 0 && sbd.charAt(sbd.length() - 1) == SPACE) {
      return;
    }
    sbd.append(c);
  }
}
