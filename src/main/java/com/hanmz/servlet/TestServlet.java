package com.hanmz.servlet;

import com.github.autoconf.helper.ConfigHelper;
import org.springframework.stereotype.Component;


@Component
public class TestServlet {
  TomcatConfig tomcatConfig = new TomcatConfig();

  TestServlet() {
    System.out.println(ConfigHelper.getProcessInfo().getIp());
    System.out.println(ConfigHelper.getProcessInfo().getPort());
    System.out.println(this.getClass().getResource("/").getPath());
  }
}
