package org.surabhi.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerWithKeys {

    private static final Logger log = LoggerFactory.getLogger(ProducerWithKeys.class.getSimpleName());

    public static void main(String[] args) {
        log.info("I am a Kafka Producer!");

        //Create Producer Properties
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");

        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());

        //Create Producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        for (int j=0;j<2;j++){
            for (int i = 0; i < 10; i++) {

                String topic = "fourthtopic";
                String key   = "id_" + i;
                String value = "hello world " + i;

                ProducerRecord<String, String> producerRecord =
                        new ProducerRecord<>(topic, key, value);

                //Send data
                producer.send(producerRecord, new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                        if (e == null) {
                            log.info("\n"+
                                    "Key:" + key + " | Partition: " + recordMetadata.partition()
                            );
                        } else {
                            log.error("Error while producing: ", e);
                        }
                    }
                });
            };
            try {
                Thread.sleep(500);
            }catch (InterruptedException e){
                log.error("Error producing: ", e);
            }
        }



        //flush and close the producer
        producer.flush();
        producer.close();
    }
}
