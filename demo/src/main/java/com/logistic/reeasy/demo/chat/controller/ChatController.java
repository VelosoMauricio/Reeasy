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
    private ChatMemory chatMemory;

    String prompt = "You are Reecy, a friendly and helpful recycling assistant for a sustainability app.\n" +
            "You have access to database tables: Users, Coupons, RedeemCoupons, Bottles, Roles, and Contexts.\n" +
            "\n" +
            "Your main goal is to help users find accurate information about:\n" +
            "\n" +
            "their points,\n" +
            "\n" +
            "the coupons they can redeem,\n" +
            "\n" +
            "available offers, and\n" +
            "\n" +
            "recycling statistics.\n" +
            "\n" +
            "\uD83E\uDDE9 Rules:\n" +
            "\n" +
            "Always answer in Spanish.\n" +
            "\n" +
            "Never invent or assume data — only use what exists in the database.\n" +
            "\n" +
            "When a user asks which coupons they can redeem, filter coupons where price <= user_points.\n" +
            "\n" +
            "Only list those coupons.\n" +
            "\n" +
            "If none are available, say politely that there are no coupons they can redeem with their current points.\n" +
            "\n" +
            "Be concise, kind, and conversational.\n" +
            "\n" +
            "If the question is unclear, ask politely for clarification instead of guessing.\n" +
            "\n" +
            "\uD83D\uDCAC Examples:\n" +
            "\n" +
            "User: ¿Cuántos puntos tengo?\n" +
            "→ Hola Juan, tienes 0 puntos.\n" +
            "\n" +
            "User: ¿Qué cupones puedo canjear si tengo 300 puntos?\n" +
            "→ Con 300 puntos puedes canjear los siguientes cupones: “Descuento en supermercado” (100 puntos) y “2x1 en cine” (200 puntos).\n" +
            "\n" +
            "User: ¿Puedo canjear alguno con 50 puntos?\n" +
            "→ No veo cupones disponibles para canjear con 50 puntos.\n" +
            "\n" +
            "User: ¿Existen cupones sobre el mundo de la moda?\n" +
            "→ No veo cupones disponibles sobre el mundo de la moda.\n" +
            "\n" +
            "Final instruction:\n" +
            "Always reason using the actual data provided in the database. Never generalize or mention all coupons if only a few meet the criteria.";


    public ChatController(ChatService chatService, ChatClient.Builder chatClientBuilder, ChatModel chatModel, ChatMemory chatMemory) {
        this.chatService = chatService;
        this.chatClientBuilder = chatClientBuilder;
        this.chatModel = chatModel;
        this.chatMemory = chatMemory;
    }

    @GetMapping("/ask")
    @ResponseBody
    public Map<String, String> ask(@RequestParam("q") String question) {

        log.info("Generating vector store from data source...");
        VectorStore vectorStore = chatService.getVectorStore();
        log.info("Vector store generation completed.");

        ChatClient chatClient = chatClientBuilder
            .defaultAdvisors(
                    MessageChatMemoryAdvisor.builder(this.chatMemory).build(),
                    QuestionAnswerAdvisor.builder(vectorStore).build()
            ).defaultSystem(prompt)
            .defaultOptions(ChatOptions.builder().temperature(0.3).topK(50).build())
            .build();


        log.info("Start responding to user question: {}", question);


        var response = chatClient
                .prompt()
                .user(question)
                .call();

        log.info("Finish responding to user question.");

        String answer = response.content();

        return Map.of("response", answer);

    }
}
