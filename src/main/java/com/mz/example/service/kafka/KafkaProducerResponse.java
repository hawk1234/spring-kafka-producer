package com.mz.example.service.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.apache.kafka.clients.producer.RecordMetadata;

@Builder
@Getter
@AllArgsConstructor
public class KafkaProducerResponse {

    private String topic;
    private int partition;
    private long offset;
    private String msgSent;

    public static KafkaProducerResponse create(RecordMetadata recordMetadata, String msgSent){
        return KafkaProducerResponse.builder()
                .topic(recordMetadata.topic())
                .partition(recordMetadata.partition())
                .offset(recordMetadata.offset())
                .msgSent(msgSent).build();
    }
}
