package kafka.produce;

import java.util.Properties;

import com.alibaba.fastjson.JSON;
import kafka.model.User;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class Produce {

    public static void main(String[] args) {
        System.out.println("begin produce");
        connectionKafka();
        System.out.println("finish produce");
    }

    public static void connectionKafka() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1000*4);   // 批量延时4s发送一次
        props.put("buffer.memory", 33554432);
        props.put("num.partitions", 6);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props);
        System.out.println("start:"+System.currentTimeMillis());
        for (int i = 0; i < 100; i++) {
            User user = new User();
            user.setName("name" + i);
            user.setAge(i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("send:"+System.currentTimeMillis());
            producer.send(new ProducerRecord<String, String>("zkTopic", Integer.toString(i),
                                                             JSON.toJSONString(user)));
        }
        producer.close();
    }

}
