package com.cn.service.kafkaservice;

/**
 * Created by cuixiaowei on 2016/11/24.
 */
public interface KafkaPartitionerservice {
    public int partition(Object key, int numPartitions);
}
