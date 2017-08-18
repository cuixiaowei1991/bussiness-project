package com.cn.service.kafkaservice;


/**
 * @author wangbin
 * 创建时间: 20151216
 * 描述：Kafka.Producer发送消息实现类
 */
public interface KafkaProducerService {

	/**
	 * 向kafka指定Topic发送json消息
	 * @param topic
	 * @param message
	 * @return
	 */
	public boolean send(String topic, String message);
	
	
		
}
