package com.example.javaLLM.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.tensorflow.*;

import java.nio.charset.StandardCharsets;

@Service
public class TextModelService {

    @Value("${model.path}")
    private String modelPath;

    @Value("${model.tag}")
    private String modelTag;

    private SavedModelBundle model;

    @PostConstruct
    public void loadModel() {
        model = SavedModelBundle.load(modelPath, modelTag);
    }

    public String generate(String inputText) {
        try (Tensor inputTensor = Tensors.create(inputText.getBytes(StandardCharsets.UTF_8))) {
            try (Tensor result = model.session()
                    .runner()
                    .feed("serving_default_input", inputTensor)  // match input name
                    .fetch("StatefulPartitionedCall")            // match output name
                    .run()
                    .get(0)) {

                byte[] outputBytes = new byte[(int) result.shape()[0]];
                result.copyTo(outputBytes);
                return new String(outputBytes, StandardCharsets.UTF_8);
            }
        }
    }
}

