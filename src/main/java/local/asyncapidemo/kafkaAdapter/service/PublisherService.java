package local.asyncapidemo.kafkaAdapter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import local.asyncapidemo.kafkaAdapter.model.S3EventPayload;
    

@Service
public class PublisherService {

    @Autowired
    private KafkaTemplate<Integer, Object> kafkaTemplate;
 
    /**
     * here s3demo publishes changelog entity objects
     */
    public void publishEvent(Integer key, S3EventPayload s3EventPayload) {
        Message<S3EventPayload> message = MessageBuilder.withPayload(s3EventPayload)
                .setHeader(KafkaHeaders.TOPIC, "s3demo.changelog.v1")
                .setHeader(KafkaHeaders.MESSAGE_KEY, key)
                .build();
        kafkaTemplate.send(message);
    }
}
