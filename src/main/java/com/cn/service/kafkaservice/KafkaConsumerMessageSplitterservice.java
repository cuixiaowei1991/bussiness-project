package com.cn.service.kafkaservice;

import org.springframework.messaging.Message;

import java.util.List;

/**
 * Created by cuixiaowei on 2016/11/24.
 */
public interface KafkaConsumerMessageSplitterservice {
    public List<Message<?>> split(Message<?> message);
}
