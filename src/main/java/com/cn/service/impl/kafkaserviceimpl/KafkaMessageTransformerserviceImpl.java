package com.cn.service.impl.kafkaserviceimpl;

import com.alibaba.fastjson.JSON;
import com.cn.service.kafkaservice.KafkaMessageTransformerservice;
import org.springframework.integration.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class KafkaMessageTransformerserviceImpl implements KafkaMessageTransformerservice {
	

	public Message<?> transformer(Message<?> message){
		MessageHeaders headers = message.getHeaders();
		String json = (String)message.getPayload();
		Object obj = JSON.parse(json);
		MessageBuilder<?> mb = MessageBuilder.withPayload(obj);
		if(headers.containsKey(KafkaHeaders.TOPIC)){
			System.out.print("kafka数据：");
			mb.setHeader(KafkaHeaders.TOPIC, headers.get(KafkaHeaders.TOPIC));
			System.out.print(json);
		}
		return mb.build();
	}
	
}
