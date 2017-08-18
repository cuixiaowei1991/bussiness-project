package com.cn.service.impl.kafkaserviceimpl;


import com.cn.service.kafkaservice.KafkaPartitionerservice;
import kafka.producer.Partitioner;
import org.springframework.stereotype.Service;


/**
 * @author liu.songgao
 * <p>创建时间: 2015年5月7日 上午11:39:55</p>
 * <p>描述：Kafka.Producer分区实现类</p>
 * @version 1.0
 */
@Service
public class KafkaPartitionerImpl implements Partitioner, KafkaPartitionerservice {

	private static int index = 0;

	/**
	 * 发送消息平均分配到不同分区上
	 * @param key 舍弃参数，不为null
	 * @param numPartitions 分区的数量
	 * @return 分区号，从0开始
	 */

	public int partition(Object key, int numPartitions) {
		int part = index % numPartitions;
		index++;
		if(index == numPartitions){
			index = 0;
		}
		return part;
	}
	
}
