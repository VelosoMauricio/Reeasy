package com.logistic.reeasy.demo.chat.di;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Bean
    VectorStore vectorStore(EmbeddingModel embeddingModel){
        return SimpleVectorStore.builder(embeddingModel).build();
    }


    @Bean
    ChatClient chatClient(ChatModel chatModel) { // <-- Inject the MODEL
        return ChatClient.builder(chatModel)
                .defaultAdvisors()
                .defaultOptions(ChatOptions.builder().topK(100).temperature(.7).build())
                .build();
    }
}