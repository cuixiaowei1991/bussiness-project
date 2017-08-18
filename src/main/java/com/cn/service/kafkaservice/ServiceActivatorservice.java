package com.cn.service.kafkaservice;

import org.springframework.messaging.Message;

/**
 * Created by cuixiaowei on 2016/11/24.
 */
public interface ServiceActivatorservice {
    /**
     * 消息处理接口
     * @param message
     */
    public void execute(Message<?> message);
}
