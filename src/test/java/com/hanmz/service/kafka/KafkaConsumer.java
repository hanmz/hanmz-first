package com.hanmz.service.kafka;

import com.github.autoconf.ConfigFactory;
import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Created by hanmz on 2016/10/31.
 */
@Slf4j
public class KafkaConsumer {
  private String topic = "hanmz-test";
  private RecordHandler recordHandler = new TestHandler();
  private Properties consumerProperties = null;
  private ScheduledExecutorService executor;
  private List<ConsumerRunner> consumerRunners = Lists.newArrayList();
  private String configId = "hanmz-kafka-consumer";

  public static void main(String[] args) throws InterruptedException {
    new KafkaConsumer();
    Thread.sleep(1000 * 60 * 60);
  }

  public KafkaConsumer() {
    ConfigFactory.getInstance().getConfig(configId, config -> {
      String consumerConfigKeys = config.get("consumer.config.keys", Joiner.on(",")
                                                                           .join("bootstrap.servers", "group.id", "fetch.min.bytes", "enable.auto.commit", "auto.commit.interval.ms", "session.timeout.ms", "connections.max.idle.ms", "key.deserializer", "value.deserializer"));
      Splitter splitter = Splitter.on(CharMatcher.anyOf(", ;")).omitEmptyStrings().trimResults();
      Properties newConsumerProperties = new Properties();
      for (String key : splitter.split(consumerConfigKeys)) {
        String val = config.get(key);
        if (!Strings.isNullOrEmpty(val)) {
          newConsumerProperties.put(key, val);
        }
      }
      consumerProperties = newConsumerProperties;
      log.info("load consumer configs:{}.", newConsumerProperties);
      int consumerCount = config.getInt("consumer.count", 0);
      log.info("consumer count:{}", consumerCount);
      if (consumerCount > 0) {
        createFixedCountConsumers(consumerCount);
      } else {
        //        createPartitionCountConsumers();
      }
    });
  }

  /**
   * 创建partition num的consumer,放到线程池中运行
   */
  private void createFixedCountConsumers(int consumerCount) {
    shutdown();
    consumerRunners.clear();

    ThreadFactory tf = new ThreadFactoryBuilder().setDaemon(true).setNameFormat("hanmz-consumer-%d").build();
    executor = Executors.newScheduledThreadPool(consumerCount, tf);
    for (int i = 0; i < consumerCount; i++) {
      org.apache.kafka.clients.consumer.KafkaConsumer consumer =
        new org.apache.kafka.clients.consumer.KafkaConsumer(consumerProperties);
      consumer.subscribe(Arrays.asList(topic));
      ConsumerRunner consumerRunner = new ConsumerRunner(consumer, recordHandler);
      consumerRunners.add(consumerRunner);
      executor.scheduleAtFixedRate(consumerRunner, 5, 60, TimeUnit.SECONDS);
    }
    Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
  }

  /**
   * 安全关闭consumer,executor
   */
  private void shutdown() {
    log.info("jvm exit, now try stop " + getClass().getSimpleName());
    //首先关闭线程池,不接受新任务,不关闭正在运行任务,不阻塞
    if (executor != null) {
      executor.shutdown();
    }
    //对正在进行的任务发出关闭信号
    if (consumerRunners.size() > 0) {
      consumerRunners.forEach(ConsumerRunner::shutdown);
    }
    //等待5秒钟,如果正在进行任务关闭失败,就强制关闭
    if (executor != null) {
      try {
        if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
          executor.shutdownNow();
        }
      } catch (InterruptedException e) {
        log.error("shutdown executor error", e);
      }
    }
  }

}
