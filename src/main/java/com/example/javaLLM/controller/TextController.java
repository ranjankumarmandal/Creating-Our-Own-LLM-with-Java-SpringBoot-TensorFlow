package com.example.javaLLM.controller;


import com.example.javaLLM.model.TextRequest;
import com.example.javaLLM.service.TextModelService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TextController {

    private final TextModelService textModelService;

    public TextController(TextModelService textModelService) {
        this.textModelService = textModelService;
    }

    @PostMapping("/generate")
    public String generateText(@RequestBody TextRequest request) {
        return textModelService.generate(request.getInput());
    }
}

