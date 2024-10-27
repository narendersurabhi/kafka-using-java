package org.surabhi.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.CooperativeStickyAssignor;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerWithCooperativeStickyAssignor {

    private static final Logger log = LoggerFactory.getLogger(ConsumerWithCooperativeStickyAssignor.class.getSimpleName());

    public static void main(String[] args) {
        log.info("I am a Kafka Consumer!");

        String groupId = "my-application";
        String topic = "fourthtopic";
        //Create Properties
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");

        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer",StringDeserializer.class.getName());

        properties.setProperty("group.id", groupId);
        properties.setProperty("auto.offset.reset", "earliest");
        properties.setProperty("partition.assignment.strategy", CooperativeStickyAssignor.class.getName());

        // Create Consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        final Thread mainThread = Thread.currentThread();

        Runtime.getRuntime().addShutdownHook(new Thread(){
            public void run(){
                log.info("Detected a shutdown, let's exit by calling consumer.wakeup()...");
                consumer.wakeup();

                try{
                    mainThread.join();
                } catch (InterruptedException e) {
                    log.error("Unexpected error: ", e.printStackTrace());
                }
            }
        });


        try {
            // Subscribe to a Topic
            consumer.subscribe(Arrays.asList(topic));

            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

                for (ConsumerRecord<String, String> consumerRecord : records) {
                    log.info("Partition: " + consumerRecord.partition() +
                            "; Key: " + consumerRecord.key() +
                            "; Value:" + consumerRecord.value() +
                            "; Offset:" + consumerRecord.offset());
                }

//                log.info("Polling..");

            }
        } catch (WakeupException wakeupException){
            log.info("Shutdown detected. Trying to shutdown.");
        } catch (Exception exception){
            log.error("Unexpected", exception);
        } finally {
            consumer.close();
            log.info("Consumer successfully shutdown!");
        }


    }
}
