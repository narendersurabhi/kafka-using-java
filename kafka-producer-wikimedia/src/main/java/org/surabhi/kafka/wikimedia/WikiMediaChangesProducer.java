package org.surabhi.kafka.wikimedia;

import com.launchdarkly.eventsource.EventSource;
import com.launchdarkly.eventsource.background.BackgroundEventSource;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import com.launchdarkly.eventsource.background.BackgroundEventHandler;
import java.net.URI;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class WikiMediaChangesProducer {
    public static void main(String[] args) throws InterruptedException {

        //Create Producer Properties
        Properties properties = new Properties();

        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        //Create Producer
        KafkaProducer<String,String> producer = new KafkaProducer<>(properties);

        String topic = "wikimedia.recentchange";

        BackgroundEventHandler eventHandler = new WikimediaChangeHandler(producer, topic);
        String url = "https://stream.wikimedia.org/v2/stream/recentchange";
        EventSource.Builder eventsourceBuilder = new EventSource.Builder(URI.create(url));
        BackgroundEventSource.Builder builder = new BackgroundEventSource.Builder(eventHandler, eventsourceBuilder);
        BackgroundEventSource eventSource = builder.build();
        try
        {
            // Start producer in another thread
            eventSource.start();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

        // Wait for 10 minutes
        TimeUnit.MINUTES.sleep(10);

    }
}
