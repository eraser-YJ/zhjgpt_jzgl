package com.jc.plugin.kafka.util;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import java.util.Arrays;
import java.util.Properties;

/**
 * 获取kafka消息
 * @Author changpeng
 * @Date 2020/7/3 14:58
 * @Version 1.0
 */
public class KafkaConsumerUtil {

    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "192.168.0.201:9092");
        properties.put("group.id", "0");
        /**
         * 消费者是否自动提交偏移量，默认是true
         * 为了经量避免重复数据和数据丢失，可以把它设为true,
         * 由自己控制核实提交偏移量。
         * 如果设置为true,可以通过auto.commit.interval.ms属性来设置提交频率
         */
        properties.put("enable.auto.commit", "true");
        /**
         * 自动提交偏移量的提交频率
         */
        properties.put("auto.commit.interval.ms", "1000");
        /**
         * 默认值latest.
         * latest:在偏移量无效的情况下，消费者将从最新的记录开始读取数据
         * erliest:偏移量无效的情况下，消费者将从起始位置读取分区的记录。
         */
        properties.put("auto.offset.reset", "earliest");
        /**
         * 消费者在指定的时间内没有发送心跳给群组协调器，就被认为已经死亡，
         * 协调器就会触发再均衡，把它的分区分配给其他消费者。
         */
        properties.put("session.timeout.ms", "30000");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);

        String topicName = "platformMessage";
        kafkaConsumer.assign(Arrays.asList(new TopicPartition(topicName, 0)));
        //设置重头调用
        kafkaConsumer.seekToBeginning(Arrays.asList(new TopicPartition(topicName, 0)));

        //设置跳转到指定offset
        //kafkaConsumer.seek(new TopicPartition(topicName, 0), 4);
        //无限循环轮询
        while (true) {
            /**
             * 消费者必须持续对Kafka进行轮询，否则会被认为已经死亡，他的分区会被移交给群组里的其他消费者。
             * poll返回一个记录列表，每个记录包含了记录所属主题的信息，
             * 记录所在分区的信息，记录在分区里的偏移量，以及键值对。
             * poll需要一个指定的超时参数，指定了方法在多久后可以返回。
             * 发送心跳的频率，告诉群组协调器自己还活着。
             */
            ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                Thread.sleep(1000);
                System.out.printf("offset = %d, value = %s", record.offset(), record.value());
                System.out.println();
            }
        }
    }
}
