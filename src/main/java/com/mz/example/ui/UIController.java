package com.mz.example.ui;

import com.mz.example.api.model.ProduceMessageRequest;
import com.mz.example.api.model.ProduceMessageResponse;
import com.mz.example.service.customization.MessageType;
import com.mz.example.service.customization.descriptor.CustomizationDescriptor;
import com.mz.example.service.customization.descriptor.GenerationType;
import com.mz.example.service.customization.descriptor.ValueTypeDescriptor;
import com.mz.example.service.general.ProduceMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Controller
public class UIController {

    @Autowired
    private ProduceMessageService produceMessageService;

    @GetMapping({"", "/"})
    public String index() {
        return "index";
    }

    @GetMapping("/ui/message")
    public String handleMessage(@RequestParam(required = false, defaultValue = "0") int customizations, Model model) {
        model.addAttribute("msgTypes", MessageType.values());
        model.addAttribute("valueTypes", ValueTypeDescriptor.ValueType.values());
        model.addAttribute("generationTypes", GenerationType.values());
        ProduceMessageRequest produceMessageRequest = new ProduceMessageRequest();
        produceMessageRequest.setCustomizationDescriptors(prepareEmptyDescriptors(customizations));
        model.addAttribute("msgRequest", produceMessageRequest);
        return "ui/message";
    }

    @PostMapping("/ui/send")
    public String handleSend(@ModelAttribute ProduceMessageRequest msgRequest,
                             Model model) throws ExecutionException, InterruptedException {
        ProduceMessageResponse pmr = produceMessageService.customizeAndSend(msgRequest);
        model.addAttribute("customizedMsg", pmr.getSentMsg());
        return "ui/send";
    }

    private List<CustomizationDescriptor<?>> prepareEmptyDescriptors(int numberOfCustomizations){
        return IntStream.range(0, numberOfCustomizations)
                .mapToObj(i -> createNew()).collect(Collectors.toList());
    }

    private CustomizationDescriptor<?> createNew(){
        return new CustomizationDescriptor<>();
    }
}
