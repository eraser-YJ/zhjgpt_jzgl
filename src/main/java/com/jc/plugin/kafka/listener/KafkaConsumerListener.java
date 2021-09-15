package com.jc.plugin.kafka.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.MessageListener;

/**
 * @ClassName KafkaConsumerListener
 * @Author Administrator
 * @Date 2020/7/3 14:23
 * @Version 1.0
 */
public class KafkaConsumerListener implements MessageListener<String, String> {
    @Override
    public void onMessage(ConsumerRecord<String, String> data) {
        String jsonValue = data.value();
        System.out.println(jsonValue);
    }
}
