package kafka.consumer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

/**
 * 创建topic命令: bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 6 --topic threadTopic
 *
 *
 * @doc http://kafka.apache.org/0100/javadoc/index.html?org/apache/kafka/clients/consumer/KafkaConsumer.html
 */
public class Consumer {

    private static final String topic = "threadTopic";

    public static void main(String[] args) {
        // multiThreadConsumer();
        // singleConsumer();
        // syncBatchCommitConsumer();
        // syncPartitionCommitConsumer();
        retryConsumer();
    }

    /**
     * 单线程消费
     */
    private static void singleConsumer() {
        KafkaConsumer<String, String> consumer = buildKafkaConsumer();
        // 订阅指定分区
        // TopicPartition partition0 = new TopicPartition(topic, 0);
        // TopicPartition partition1 = new TopicPartition(topic, 1);
        // consumer.assign(Arrays.asList(partition0, partition1));

        while (true) {

            ConsumerRecords<String, String> records = consumer.poll(100);
            System.out.println("开始拉取数据:" + records.count());
            for (ConsumerRecord<String, String> record : records) {
                System.out.println("消费" + record.value() + ",分区:" + record.partition());
            }
        }
    }

    /**
     * 多线程消费(记得设置topic partitions) 线程数要小于等于分区数
     */
    private static void multiThreadConsumer() {
        KafkaConsumer<String, String> consumer = buildKafkaConsumer();
        KafkaConsumerRunner runner = new KafkaConsumerRunner(consumer, topic);
        for (int i = 0; i < 5; i++) {
            new Thread(runner, "thread-" + i).start();
        }

        // must down
        // runner.shutdown();
    }

    /**
     * 手动批量提交
     */
    private static void syncBatchCommitConsumer() {
        final int minBatchSize = 200;
        KafkaConsumer<String, String> consumer = buildSycnKafkaConsumer();
        List<ConsumerRecord<String, String>> buffer = new ArrayList<>();
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                buffer.add(record);
                System.out.println("手动提交消费:" + record.value());
            }
            if (buffer.size() >= minBatchSize) {
                consumer.commitSync();
                buffer.clear();
            }
        }
    }

    /**
     * 消费失败,移动offset重新消费
     */
    private static void retryConsumer() {
        KafkaConsumer<String, String> consumer = buildSycnKafkaConsumer();
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(100);
                retryOffset: for (TopicPartition partition : records.partitions()) {
                    List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
                    for (ConsumerRecord<String, String> record : partitionRecords) {
                        if (mockConsumer()) {
                            System.out.println("分区提交消费成功:" + record.offset() + "——" + record.value());
                        } else {
                            // Controlling The Consumer's Position
                            consumer.seek(partition, record.offset());
                            System.out.println("分区提交消费失败, 重新消费:" + record.offset() + "——" + record.value());
                            continue retryOffset;
                        }
                    }
                    long lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
                    consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(lastOffset + 1)));
                }
            }
        } finally {
            consumer.close();
        }
    }

    /**
     * 模拟处理失败和成功
     * 
     * @return true / false
     */
    private static boolean mockConsumer() {
        Random random = new Random();
        if (random.nextInt(100) % 2 == 0) {
            return true;
        }
        return false;
    }

    /**
     * 手动分区提交
     */
    private static void syncPartitionCommitConsumer() {
        KafkaConsumer<String, String> consumer = buildSycnKafkaConsumer();
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (TopicPartition partition : records.partitions()) {
                    List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
                    for (ConsumerRecord<String, String> record : partitionRecords) {
                        System.out.println("分区提交消费:" + record.offset() + "—— " + record.value());
                    }
                    long lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
                    consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(lastOffset + 1)));

                    // Controlling The Consumer's Position
                    consumer.seek(partition, 0);
                }
            }
        } finally {
            consumer.close();
        }

    }

    /**
     * 构建KafkaConsumer
     * 
     * @return KafkaConsumer
     */
    private static KafkaConsumer<String, String> buildKafkaConsumer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "autoCommit");
        props.put("enable.auto.commit", "true");
        props.put("auto.offset.reset", "latest");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(topic));

        return consumer;
    }

    /**
     * 构建手工提交KafkaConsumer
     *
     * @return KafkaConsumer
     */
    private static KafkaConsumer<String, String> buildSycnKafkaConsumer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "handCommit");
        props.put("enable.auto.commit", "false");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(topic));

        return consumer;
    }

}
