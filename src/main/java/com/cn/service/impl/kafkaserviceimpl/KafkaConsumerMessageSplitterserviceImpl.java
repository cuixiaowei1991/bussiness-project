package com.cn.service.impl.kafkaserviceimpl;


import com.cn.service.kafkaservice.KafkaConsumerMessageSplitterservice;
import org.springframework.integration.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @创建人 liu.songgao
 * @创建时间 2015-3-26
 * @Kafka.Consumer消费消息分解器
 */
@Service
public class KafkaConsumerMessageSplitterserviceImpl implements KafkaConsumerMessageSplitterservice {

	/**
	 * 分解kafka的消息Map,并将Topic信息存入Message的Header中，用于之后的消息路由
	 * @param message kafka订阅到的消息
	 * @return 分解后的消息List
	 */
	public List<Message<?>> split(Message<?> message){
		List<Message<?>> msgList = new ArrayList<Message<?>>();
		Map<String, Map<Integer, List<Object>>> msgMap = (Map<String, Map<Integer, List<Object>>>) message.getPayload();
		Iterator<String> it_Topic = msgMap.keySet().iterator();
		while (it_Topic.hasNext()) {
			String topic = it_Topic.next();
			Map<Integer, List<Object>> map_Message = msgMap.get(topic);
			Iterator<Integer> it_Index = map_Message.keySet().iterator();
			while (it_Index.hasNext()) {
				Integer partIndex = it_Index.next();
				List<Object> list = map_Message.get(partIndex);
				for(Object obj : list){
					String msgString = (String)obj;
					MessageBuilder<?> mb = MessageBuilder.withPayload(msgString);
					mb.setHeader(KafkaHeaders.TOPIC, topic);
					Message<?> msg  = mb.build();
					msgList.add(msg);
				}
			}
		}
		return msgList;
	}

}
