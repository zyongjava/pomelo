package kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class KafkaConsumerRunner implements Runnable {

    private final AtomicBoolean closed = new AtomicBoolean(false);
    private KafkaConsumer       consumer;

    private String              topic;

    public KafkaConsumerRunner(KafkaConsumer consumer, String topic){
        this.consumer = consumer;
        this.topic = topic;
    }

    public void run() {
        try {
            consumer.subscribe(Arrays.asList(topic));
            while (!closed.get()) {
                ConsumerRecords<String, String> records = consumer.poll(10000);
                // Handle new records
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(Thread.currentThread().getName() +"线程消费" + record.value() + ",分区:" + record.partition());
                }
            }
        } catch (WakeupException e) {
            // Ignore exception if closing
            if (!closed.get()) throw e;
        } finally {
            consumer.close();
        }
    }

    // Shutdown hook which can be called from a separate thread
    public void shutdown() {
        closed.set(true);
        consumer.wakeup();
    }
}
