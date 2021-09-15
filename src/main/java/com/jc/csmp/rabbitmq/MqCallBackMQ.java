package com.jc.csmp.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;

/**
 * @Author 常鹏
 * @Date 2020/9/11 13:57
 * @Version 1.0
 */
public class MqCallBackMQ implements RabbitTemplate.ConfirmCallback {
    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        if (b) {
            //设置消息投递成功
            System.out.println("消息投递成功");
        } else {
            //消息投递失败
            System.out.println("消息投递失败");
        }
    }
}
