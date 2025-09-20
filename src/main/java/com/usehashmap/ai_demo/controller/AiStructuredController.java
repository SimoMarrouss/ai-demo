package com.usehashmap.ai_demo.controller;

import com.usehashmap.ai_demo.output.MovieList;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AiStructuredController {

    private final ChatClient chatClient;

    public AiStructuredController(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    @GetMapping("/ask")
    public MovieList askLLM(String message) {
        return chatClient.prompt()
                .system("""
                            You are a cinema expert
                            Answer the user for his queries
                        """)
                .user(message)
                .call()
                .entity(MovieList.class);


    }


}
