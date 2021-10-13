package local.asyncapidemo.kafkaAdapter;

 
import local.asyncapidemo.kafkaAdapter.model.S3EventPayload;
 
  
import local.asyncapidemo.kafkaAdapter.service.PublisherService;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.connect.json.JsonDeserializer;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.shaded.com.google.common.collect.Lists;

import java.time.Duration;
import java.util.*;

import static java.util.Collections.emptyList;
import static org.apache.kafka.clients.consumer.ConsumerConfig.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Example of tests for kafka based on testcontainers library
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestcontainerKafkaTest {

     
    private static final String PUBLISHEVENT_TOPIC = "s3demo.changelog.v1";
      
    @ClassRule
    public static KafkaContainer kafka = new KafkaContainer();
    
    @Autowired
    private PublisherService publisherService;
    
    @DynamicPropertySource
    public static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }
     
    @Test
    public void publishEventProducerTestcontainers() {
        S3EventPayload payload = new S3EventPayload();
        Integer key = 1;
        Integer wrongKey = key + 1;

        consumeMessages(PUBLISHEVENT_TOPIC);

        publisherService.publishEvent(key, payload);

        ConsumerRecord<Integer, Object> consumedMessage = consumeMessage(PUBLISHEVENT_TOPIC);

        assertEquals("Key is wrong", key, consumedMessage.key());
        assertNotEquals("Key is wrong", wrongKey, consumedMessage.key());
    }
     
    
    
    protected ConsumerRecord<Integer, Object> consumeMessage(String topic) {
        return consumeMessages(topic)
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("no message received"));
    }

    protected List<ConsumerRecord<Integer, Object>> consumeMessages(String topic) {
        try (KafkaConsumer<Integer, Object> consumer = createConsumer(topic)) {
            return pollForRecords(consumer);
        }
    }

    protected KafkaConsumer<Integer, Object> createConsumer(String topic) {
        Properties properties = new Properties();
        properties.putAll(getKafkaConsumerConfiguration());
        KafkaConsumer<Integer, Object> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singleton(topic));
        return consumer;
    }

    protected static <K, V> List<ConsumerRecord<K, V>> pollForRecords(KafkaConsumer<K, V> consumer) {
        ConsumerRecords<K, V> received = consumer.poll(Duration.ofSeconds(10L));
        return received == null ? emptyList() : Lists.newArrayList(received);
    }

    protected Map<String, Object> getKafkaConsumerConfiguration() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        configs.put(GROUP_ID_CONFIG, "testGroup");
        configs.put(AUTO_OFFSET_RESET_CONFIG, "earliest");
        configs.put(KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class.getName());
        configs.put(VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
        return configs;
    }
    
}
