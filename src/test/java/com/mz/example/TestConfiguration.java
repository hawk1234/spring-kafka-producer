package com.mz.example;

import com.mz.example.service.kafka.KafkaProducerService;
import com.mz.example.service.kafka.KafkaProducerResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CompletableFuture;

@Configuration
public class TestConfiguration {

    private static final int PARTITION = 0;
    private static final int OFFSET = 0;

    @Bean
    public KafkaProducerService kafkaProducerService(){
        return (topic, msg) -> CompletableFuture.completedFuture(new KafkaProducerResponse(topic, PARTITION, OFFSET, msg));
    }
}
