package com.usehashmap.ai_demo.controller;

import com.usehashmap.ai_demo.output.Cin;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
public class MultiModelAiController {

    private final ChatClient chatClient;

    @Value("classpath:/img/cin.png")
    private Resource resource;

    @Value("classpath:/img/recipe.jpeg")
    private Resource resource2;

    public MultiModelAiController(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    @GetMapping("/describe")
    public Cin describe() {
        return chatClient.prompt()
                .user("Give the detail about this identity card")
                .user(u -> u.text("Give the detail about this identity card")
                        .media(MediaType.IMAGE_PNG, resource)
                )
                .call()
                .entity(Cin.class);

    }

    @GetMapping("/askImage")
    public String askImage(String message) {
        return chatClient.prompt()
                .user("Extract the hand written text in the attached image, and answer the user based on his query")
                .user(u -> u.text(message)
                        .media(MediaType.IMAGE_PNG, resource2)
                )
                .call()
                .content();

    }

    @PostMapping(value = "/askImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String askForImage(String message, @RequestPart MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
        return chatClient.prompt()
                .user("Extract the hand written text in the attached image, and answer the user based on his query")
                .user(u -> u.text(message)
                        .media(MediaType.IMAGE_PNG, file.getResource())
                )
                .call()
                .content();

    }


}
