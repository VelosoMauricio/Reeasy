package com.logistic.reeasy.demo.chat.service;

import com.logistic.reeasy.demo.chat.dto.ChatResponseDto;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.ai.document.Document;


import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ChatService {

    private ChatClient.Builder chatClientBuilder;
    private ChatModel chatModel;
    private ChatMemory chatMemory;

    @Value("${export.file.path}")
    private Resource fileResource;

    @Getter
    private VectorStore vectorStore;

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


    public ChatService(VectorStore vectorStore, ChatClient.Builder chatClientBuilder, ChatModel chatModel, ChatMemory chatMemory){
        this.vectorStore = vectorStore;
        this.chatClientBuilder = chatClientBuilder;
        this.chatModel = chatModel;
        this.chatMemory = chatMemory;
    }

    public ChatResponseDto getChatResponse(String question){

        ChatClient chatClient = chatClientBuilder
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(this.chatMemory).build(),
                        QuestionAnswerAdvisor.builder(vectorStore).build()
                ).defaultSystem(prompt)
                .defaultOptions(ChatOptions.builder().temperature(0.3).topK(500).build())
                .build();


        log.info("Start responding to user question: {}", question);

        var response = chatClient
                .prompt()
                .user(question)
                .call();

        String answer = response.content();

        log.info("Finish responding to user question. response: {}", answer);

        return new ChatResponseDto(answer);
    }

    @PostConstruct
    public void loadDataOnStartup() {
        log.info("Generating vector store from data source...");

        TextReader textReader = new TextReader(fileResource);
        textReader.getCustomMetadata().put("filename", "sample.txt");
        List<Document> documentsText = textReader.get();

        List<Document> documents = new TokenTextSplitter().apply(documentsText);

        vectorStore.add(documents);
        log.info("Vector store generation completed, total documents added: {}", documents.size());
    }


    @Deprecated
    public VectorStore loadDataInVectorStore(){

        TextReader textReader = new TextReader(fileResource);

        textReader.getCustomMetadata().put("filename", "sample.txt");

        List<Document> documentsText = textReader.get();

        log.info("Total documents counts are : {}", documentsText.size());

        List<Document> documents = new TokenTextSplitter().apply(documentsText);

        log.info("Total documents counts are : {}", documents.size());

        // add document to vector store
        vectorStore.add(documents);

        return vectorStore;
    }
}
