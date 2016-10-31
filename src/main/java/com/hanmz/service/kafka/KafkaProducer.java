package com.hanmz.service.kafka;

import com.alibaba.fastjson.JSON;
import com.fxiaoke.support.KafkaSender;
import com.fxiaoke.support.SenderManager;
import com.github.autoconf.base.ProcessInfo;
import com.github.autoconf.helper.ConfigHelper;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Created by hanmz on 2016/10/31.
 */
public class KafkaProducer implements AutoCloseable {
  private ScheduledExecutorService schedule;
  private ProcessInfo info = ConfigHelper.getProcessInfo();
  private KafkaSender sender;

  public static void main(String[] args) {
    new KafkaProducer().start();
  }

  public void start() {
    ThreadFactory tf = new ThreadFactoryBuilder().setDaemon(true).setNameFormat("hanmz-producer-%d").build();
    this.schedule = Executors.newSingleThreadScheduledExecutor(tf);
    schedule.scheduleAtFixedRate(this::reportUsage, 5, 5, TimeUnit.MINUTES);
    Runtime.getRuntime().addShutdownHook(new Thread(this::close));
  }

  private void reportUsage() {
    if (sender == null) {
      sender = SenderManager.getSender("kafka-log-center");
    }
    for (int i = 0; i < 100; i++) {
      try {
        sender.send("hanmz-test", JSON.toJSONString(i));
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void close() {
    schedule.shutdownNow();
  }
}
