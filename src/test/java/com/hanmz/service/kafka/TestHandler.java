package com.hanmz.service.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecords;

/**
 * 实际kafka消息处理器
 * 处理topic:config-report中的消息
 * <p>
 * Created by hanmz on 16/7/21.
 */
public class TestHandler implements RecordHandler {

  public void handle(ConsumerRecords<String, String> records) {
    System.out.println("拿到任务：" + records.count() + "个");
    System.out.println("处理中......");
    if (records.count() > 0) {
      records.forEach(item -> System.out.println(item.value()));
    }
    System.out.println("处理完成。");
  }
}
