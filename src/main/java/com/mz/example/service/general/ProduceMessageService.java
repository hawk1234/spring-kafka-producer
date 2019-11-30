package com.mz.example.service.general;

import com.mz.example.api.model.ProduceMessageRequest;
import com.mz.example.api.model.ProduceMessageResponse;
import com.mz.example.service.customization.MessageCustomizationService;
import com.mz.example.service.customization.MessageCustomizationServiceFactory;
import com.mz.example.service.kafka.KafkaProducerResponse;
import com.mz.example.service.kafka.KafkaProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

@Slf4j
@Service
public class ProduceMessageService {

    @Autowired
    private MessageCustomizationServiceFactory customizationServiceFactory;
    @Autowired
    private KafkaProducerService kafkaProducerService;

    //TODO: make request async?
    public ProduceMessageResponse customizeAndSend(ProduceMessageRequest msgRequest) throws ExecutionException, InterruptedException {
        long time = System.currentTimeMillis();
        KafkaProducerResponse kpr = IntStream.range(0, msgRequest.getAmount()).parallel().mapToObj(i -> {
            MessageCustomizationService service = customizationServiceFactory.getMessageCustomizationService(msgRequest.getMsgType());
            String customizedMsg = service.customizeMessage(msgRequest.getMsg(), msgRequest.getCustomizationDescriptors());
            return kafkaProducerService.send(msgRequest.getTopic(), customizedMsg);
        }).reduce((first, second) -> second).orElseThrow(() ->
                new NoSuchElementException("Request improperly configured. There where no messages sent to Kafka.")).get();
        log.info("Sent "+msgRequest.getAmount()+" messages in "+(System.currentTimeMillis() - time)+"ms.");
        return ProduceMessageResponse.builder().topic(kpr.getTopic())
                .partition(kpr.getPartition()).offset(kpr.getOffset()).sentMsg(kpr.getMsgSent()).build();
    }
}
