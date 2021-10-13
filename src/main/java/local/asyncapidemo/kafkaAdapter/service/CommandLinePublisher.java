package local.asyncapidemo.kafkaAdapter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CommandLinePublisher implements CommandLineRunner {

    @Autowired
    PublisherService publisherService;

    @Override
    public void run(String... args) {
        System.out.println("******* Sending message: *******");
        publisherService.publishEvent((new Random()).nextInt(), new local.asyncapidemo.kafkaAdapter.model.S3EventPayload());
            
        System.out.println("Message sent");
    }
}
