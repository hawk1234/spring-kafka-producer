package com.mz.example.api.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProduceMessageResponse {

    private String topic;
    private int partition;
    private long offset;
    private String sentMsg;
}
