package com.hanmz.servlet;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by hanmz on 2016/8/22.
 */
public class TomcatConfig{

  public void getHttpPort() {
    try {
      MBeanServer server = null;
      if (MBeanServerFactory.findMBeanServer(null).size() > 0) {
        server = MBeanServerFactory.findMBeanServer(null).get(0);
      }

      Set names = server.queryNames(new ObjectName("Catalina:type=Connector,*"), null);

      Iterator iterator = names.iterator();
      ObjectName name;
      while (iterator.hasNext()) {
        name = (ObjectName) iterator.next();

        String protocol = server.getAttribute(name, "protocol").toString();
        String scheme = server.getAttribute(name, "scheme").toString();
        String port = server.getAttribute(name, "port").toString();
        System.out.println(protocol + " : " + scheme + " : " + port);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
