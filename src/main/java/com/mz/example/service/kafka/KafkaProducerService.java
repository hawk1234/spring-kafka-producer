package com.mz.example.service.kafka;

import java.util.concurrent.Future;

public interface KafkaProducerService {

    Future<KafkaProducerResponse> send(String topic, String msg);
}
