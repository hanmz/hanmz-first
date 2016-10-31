package com.hanmz.service.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * kafka消息实际消费者，消费topic config-update和config-report
 * Created by hanmz on 2016/7/20.
 */
@Slf4j
public class ConsumerRunner implements Runnable {
  private AtomicBoolean closed = new AtomicBoolean(false);
  private final KafkaConsumer<String, String> consumer;
  private final RecordHandler recordHandler;

  /**
   * @param consumer      kafka 消费者
   * @param recordHandler 消息处理器
   */
  public ConsumerRunner(KafkaConsumer consumer, RecordHandler recordHandler) {
    this.consumer = consumer;
    this.recordHandler = recordHandler;
  }

  @Override
  public void run() {
    try {
      while (!closed.get()) {
        // 轮询，最大阻塞时间1分钟
        ConsumerRecords records = consumer.poll(1000 * 10);
        recordHandler.handle(records);
      }
    } catch (WakeupException e) {
      log.warn("consumer wake up.", e);
    } catch (Throwable e) {
      log.error("consume kafka message error", e);
    } finally {
      if (null != consumer && closed.get()) {
        log.warn("consumer closed");
        consumer.close();
      }
    }
  }

  public void shutdown() {
    closed.set(true);
    if (null != consumer) {
      consumer.wakeup();
      log.warn("consumer shutdown.");
    }
  }
}
