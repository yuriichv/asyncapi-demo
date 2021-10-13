package local.asyncapidemo.kafkaAdapter;

 
import local.asyncapidemo.kafkaAdapter.model.S3EventPayload;
 
  
import local.asyncapidemo.kafkaAdapter.service.PublisherService;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.util.AssertionErrors.assertEquals;

/**
 * Example of tests for kafka based on spring-kafka-test library
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SimpleKafkaTest {
     
    private static final String PUBLISHEVENT_TOPIC = "s3demo.changelog.v1";
      
    @ClassRule
    public static EmbeddedKafkaRule embeddedKafka = new EmbeddedKafkaRule(1, false, 1, PUBLISHEVENT_TOPIC);

    private static EmbeddedKafkaBroker embeddedKafkaBroker = embeddedKafka.getEmbeddedKafka();

    @DynamicPropertySource
    public static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", embeddedKafkaBroker::getBrokersAsString);
    }

    
    @Autowired
    private PublisherService publisherService;
     
    Consumer<Integer, S3EventPayload> consumerS3DemoChangelogV1;
       
    @Before
    public void init() {
        
        Map<String, Object> consumerConfigs = new HashMap<>(KafkaTestUtils.consumerProps("consumer", "true", embeddedKafkaBroker));
        consumerConfigs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
         
        consumerS3DemoChangelogV1 = new DefaultKafkaConsumerFactory<>(consumerConfigs, new IntegerDeserializer(), new JsonDeserializer<>(S3EventPayload.class)).createConsumer();
        consumerS3DemoChangelogV1.subscribe(Collections.singleton(PUBLISHEVENT_TOPIC));
        consumerS3DemoChangelogV1.poll(Duration.ZERO);
           
    }
     
    @Test
    public void publishEventProducerTest() {
        S3EventPayload payload = new S3EventPayload();
        Integer key = 1;

        KafkaTestUtils.getRecords(consumerS3DemoChangelogV1);

        publisherService.publishEvent(key, payload);

        ConsumerRecord<Integer, S3EventPayload> singleRecord = KafkaTestUtils.getSingleRecord(consumerS3DemoChangelogV1, PUBLISHEVENT_TOPIC);

        assertEquals("Key is wrong", key, singleRecord.key());
    }
         
    
}
