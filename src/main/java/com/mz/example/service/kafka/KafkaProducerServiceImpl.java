package com.mz.example.service.kafka;

import com.mz.example.util.MappingFuture;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Slf4j
@Service
class KafkaProducerServiceImpl implements KafkaProducerService{

    @Autowired
    private Properties kafkaProperties;
    private KafkaProducer<String, String> kafkaProducer;

    @PostConstruct
    private void init() {
        kafkaProducer = new KafkaProducer<>(kafkaProperties);
    }

    @PreDestroy
    private void dispose() {
        if(kafkaProducer != null) {
            try {
                kafkaProducer.close();
            } catch (Throwable any) {
                log.error("Error closing kafka producer ", any);
            }
        }
    }

    public KafkaProducerResponse sendAndWait(String topic, String msg) throws ExecutionException, InterruptedException {
        return send(topic, msg).get();
    }

    public Future<KafkaProducerResponse> send(String topic, String msg) {
        ProducerRecord<String, String> producerRecord =
                new ProducerRecord<>(topic, null, msg);
        return MappingFuture.create(kafkaProducer.send(producerRecord), rm -> KafkaProducerResponse.create(rm, msg));
    }
}
