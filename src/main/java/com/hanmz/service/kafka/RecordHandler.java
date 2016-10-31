package com.hanmz.service.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecords;

/**
 * 测试kafka消息处理器接口
 * 对接收到的kafka消息的处理过程
 * Created by hanmz on 16/7/20
 */
interface RecordHandler {
  void handle(ConsumerRecords<String, String> records);
}
