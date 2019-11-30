package com.mz.example.api;

import com.mz.example.api.model.ProduceMessageRequest;
import com.mz.example.service.general.ProduceMessageService;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.concurrent.ExecutionException;

@Slf4j
@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    private ProduceMessageService produceMessageService;

    @PostMapping(RestControllerMapping.PRODUCE_METHOD)
    public ResponseEntity produceNewMessage(@RequestBody ProduceMessageRequest request) throws ExecutionException, InterruptedException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(produceMessageService.customizeAndSend(request));
    }

    @UtilityClass
    public static class RestControllerMapping {

        private static final String BASE_PATH = "/api";

        public static final String PRODUCE_METHOD = BASE_PATH+"/produce";
    }
}
