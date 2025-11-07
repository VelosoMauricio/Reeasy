package com.logistic.reeasy.demo.chat.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.ai.document.Document;


import java.util.List;

@Service
@Slf4j
public class ChatService {

    @Value("classpath:/coupons.txt")
    private Resource fileResource;

    VectorStore vectorStore;

    public ChatService(VectorStore vectorStore){
        this.vectorStore = vectorStore;
    }

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
