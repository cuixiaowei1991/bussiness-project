package com.cn.service.kafkaservice;

import org.springframework.messaging.Message;

/**
 * Created by cuixiaowei on 2016/11/24.
 */
public interface KafkaMessageTransformerservice {
    public Message<?> transformer(Message<?> message);
}
