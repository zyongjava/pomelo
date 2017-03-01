package kafka.consumer;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class Consumer {

    private static final String topic = "go-topic";

    public static void main(String[] args) {
        mutiThreadConsumer();
        singleConsumer();
    }

    /**
     * 单线程消费
     */
    private static void singleConsumer() {
        KafkaConsumer<String, String> consumer = buildKafkaConsumer();
        consumer.subscribe(Arrays.asList(topic));
        // 订阅指定分区
        // TopicPartition partition0 = new TopicPartition(topic, 0);
        // TopicPartition partition1 = new TopicPartition(topic, 1);
        // consumer.assign(Arrays.asList(partition0, partition1));

        while (true) {

            ConsumerRecords<String, String> records = consumer.poll(1000 * 60);
            System.out.println("开始拉取数据:" + records.count());
            for (ConsumerRecord<String, String> record : records) {
                System.out.println("消费" + record.value() + ",分区:" + record.partition());
            }
        }
    }

    /**
     * 多线程消费(记得设置topic partitions)
     */
    private static void mutiThreadConsumer() {
        KafkaConsumer<String, String> consumer = buildKafkaConsumer();
        KafkaConsumerRunner runner = new KafkaConsumerRunner(consumer, topic);
        for (int i = 0; i < 5; i++) {
            new Thread(runner, "thread-" + i).start();
        }

        // must down
        // runner.shutdown();
    }

    /**
     * 构建KafkaConsumer
     * 
     * @return KafkaConsumer
     */
    private static KafkaConsumer<String, String> buildKafkaConsumer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "testConsumer1");
        props.put("enable.auto.commit", "true");
        props.put("auto.offset.reset", "latest");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        return consumer;
    }

}
