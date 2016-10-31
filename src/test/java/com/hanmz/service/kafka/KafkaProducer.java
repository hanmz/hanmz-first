package com.hanmz.service.kafka;

import com.alibaba.fastjson.JSON;
import com.fxiaoke.support.KafkaSender;
import com.fxiaoke.support.SenderManager;
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
  private KafkaSender sender;

  public static void main(String[] args) throws InterruptedException {
    new KafkaProducer().start();
    Thread.sleep(1000 * 60 * 60);
  }

  private void start() {
    ThreadFactory tf = new ThreadFactoryBuilder().setDaemon(true).setNameFormat("hanmz-producer-%d").build();
    this.schedule = Executors.newSingleThreadScheduledExecutor(tf);
    // 每120秒执行一次
    schedule.scheduleAtFixedRate(this::produce, 5, 120, TimeUnit.SECONDS);
    Runtime.getRuntime().addShutdownHook(new Thread(this::close));
  }

  private void produce() {
    if (sender == null) {
      sender = SenderManager.getSender("hanmz-kafka");
    }
    for (int i = 0; i < 100; i++) {
      try {
        System.out.println(i);
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
