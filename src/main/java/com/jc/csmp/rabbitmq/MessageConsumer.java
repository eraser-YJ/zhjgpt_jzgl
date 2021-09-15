package com.jc.csmp.rabbitmq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;

/**
 * @Author 常鹏
 * @Date 2020/8/28 15:20
 * @Version 1.0
 */
@Component
public class MessageConsumer implements MessageListener {

    @Override
    public void onMessage(Message message) {
        System.out.println("********");
        System.out.println(new String(message.getBody()));
        System.out.println("********");
    }
}
