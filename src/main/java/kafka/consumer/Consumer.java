package kafka.consumer;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class Consumer {

    public static void main(String[] args) {
        System.out.println("begin consumer");
        connectionKafka();
        System.out.println("finish consumer");
    }

    @SuppressWarnings("resource")
    public static void connectionKafka() {

        String topic = "my-topic";

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "testConsumer");
        props.put("enable.auto.commit", "false");
        props.put("auto.offset.reset", "none");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(topic, "test-topic"));

        // 订阅指定分区
        // TopicPartition partition0 = new TopicPartition(topic, 0);
        // TopicPartition partition1 = new TopicPartition(topic, 1);
        // consumer.assign(Arrays.asList(partition0, partition1));

        while (true) {

			ConsumerRecords<String, String> records = consumer.poll(1000);
			System.out.println("开始拉取数据:"+records.count());
            for (ConsumerRecord<String, String> record : records) {
                if (consumeData(record)) {
                    consumer.commitSync();
                }
            }
        }
    }

    private static boolean consumeData(ConsumerRecord<String, String> record) {
        if (Math.random() > 0.5) {
            System.out.println(String.format("消费失败: offset = %d, key = %s, value = %s", record.offset(), record.key(), record.value()));
            return false;
        }
		System.out.println(String.format("offset = %d, key = %s, value = %s", record.offset(), record.key(), record.value()));
        return true;
    }
}
