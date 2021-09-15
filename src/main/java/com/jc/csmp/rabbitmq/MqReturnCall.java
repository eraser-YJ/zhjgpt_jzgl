package com.jc.csmp.rabbitmq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @Author 常鹏
 * @Date 2020/9/11 13:57
 * @Version 1.0
 */
public class MqReturnCall implements RabbitTemplate.ReturnCallback {
    /**
     *只有消息从Exchange路由到Queue失败才会回调这个方法
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        System.out.println("消息从Exchage路由到Queue失败");
    }
}
