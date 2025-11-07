package com.logistic.reeasy.demo.chat.controller;

import com.logistic.reeasy.demo.chat.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.Optional;

@Controller
@Slf4j
public class ChatController {

    private ChatService chatService;
    private ChatClient chatClient;

    public ChatController(ChatService chatService, ChatClient chatClient){
        this.chatService = chatService;
        this.chatClient = chatClient;
    }

    @GetMapping("/ask")
    @ResponseBody
    public Map<String, String> ask(@RequestParam("q") String question) {

        VectorStore vectorStore = chatService.loadDataInVectorStore();

        log.info("Start responding to user question: {}", question);

        var response = chatClient
                .prompt("You are a helpful assistant that helps users find information about coupons based on the provided context.")
                .advisors(new QuestionAnswerAdvisor(vectorStore))
                .user(question)
                .call();

        log.info("Finish responding to user question.");

        System.out.println("Respuesta del modelo: " + response.content());

        String answer = response.content();

        return Map.of("response", answer);
    }
}
