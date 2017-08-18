package com.cn.service.impl.kafkaserviceimpl;


import com.cn.common.LogHelper;
import com.cn.service.kafkaservice.KafkaProducerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.kafka.support.KafkaHeaders;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;


/**
 * @author cuixiaowei
 * 创建时间: 20161124
 * 描述：Kafka.Producer发送消息实现类
 */
@Service("kafkaProducerService")
public class KafkaProducerServiceImpl implements KafkaProducerService {
	@Qualifier("outToKafka")
	@Autowired
	private MessageChannel messageChannel;
	
	/**
	 * 向kafka指定Topic发送json消息
	 */

	public boolean send(String topic, String message) {
		LogHelper.info("接收到的要推送至kafka的数据是：kafka producer[" + topic + "]:" + message);
		MessageBuilder<?> mb = MessageBuilder.withPayload(message);
		mb.setHeader(KafkaHeaders.TOPIC, topic);
		mb.setHeader(KafkaHeaders.MESSAGE_KEY, "");
		return messageChannel.send(mb.build());
	}

	/*public MessageChannel getMessageChannel() {
		return messageChannel;
	}

	public void setMessageChannel(MessageChannel messageChannel) {
		this.messageChannel = messageChannel;
	}
*/
	
	
}