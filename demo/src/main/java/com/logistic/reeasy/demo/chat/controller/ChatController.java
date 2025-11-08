package com.logistic.reeasy.demo.chat.controller;

import com.logistic.reeasy.demo.chat.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@Slf4j
public class ChatController {

    private ChatService chatService;
    private ChatClient.Builder chatClientBuilder;
    private ChatModel chatModel;

    public ChatController(ChatService chatService, ChatClient.Builder chatClientBuilder, ChatModel chatModel) {
        this.chatService = chatService;
        this.chatClientBuilder = chatClientBuilder;
        this.chatModel = chatModel;
    }

    @GetMapping("/ask")
    @ResponseBody
    public Map<String, String> ask(@RequestParam("q") String question) {

        log.info("Generating vector store from data source...");
        VectorStore vectorStore = chatService.loadDataInVectorStore();
        log.info("Vector store generation completed.");

        ChatMemory chatMemory = MessageWindowChatMemory.builder().build();

        ChatClient chatClient = chatClientBuilder
            .defaultAdvisors(
                    MessageChatMemoryAdvisor.builder(chatMemory).build(),
                    QuestionAnswerAdvisor.builder(vectorStore).build()
            )
            .defaultOptions(ChatOptions.builder().temperature(0.7).build())
            .build();


        log.info("Start responding to user question: {}", question);

        var response = chatClient
                .prompt("You are a helpful assistant that helps users find information about coupons based on the provided context.")
                .user(question)
                .call();

        log.info("Finish responding to user question.");

        String answer = response.content();

        return Map.of("response", answer);

    }
}
