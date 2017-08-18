package com.cn.service.impl.kafkaserviceimpl;

import com.alibaba.fastjson.JSONObject;
import com.cn.service.kafkaservice.ServiceActivatorservice;
import org.springframework.messaging.Message;

/**
 * Created by cuixiaowei on 2016/11/24.
 */
public class ServiceActivatorserviceImpl implements ServiceActivatorservice {
    public void execute(Message<?> message) {

            JSONObject jsObj = (JSONObject)message.getPayload();


            System.out.println("=="+jsObj.toJSONString());

        }
}

