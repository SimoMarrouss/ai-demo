package com.usehashmap.ai_demo.controller;

import org.springframework.ai.image.ImageOptions;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageAiController {

    private final OpenAiImageModel openAiImageModel;

    public ImageAiController(OpenAiImageModel openAiImageModel) {
        this.openAiImageModel = openAiImageModel;
    }

    @GetMapping("/generateImage")
    public String generateImage(String message) {
        ImageOptions imageOptions = OpenAiImageOptions.builder()
                .quality("hd")
                .model("dall-e-3")
                .build();
        ImagePrompt imagePrompt = new ImagePrompt(message, imageOptions);

        return openAiImageModel.call(imagePrompt)
                .getResult()
                .getOutput()
                .getUrl();

    }


}
