package com.logistic.reeasy.demo.chat.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.ai.document.Document;


import javax.annotation.PostConstruct;
import java.util.List;

@Service
@Slf4j
public class ChatService {

    @Value("${export.file.path}")
    private Resource fileResource;

    private VectorStore vectorStore;

    public ChatService(VectorStore vectorStore){
        this.vectorStore = vectorStore;
    }

    @PostConstruct
    public void loadDataOnStartup() {
        log.info("Cargando datos en el VectorStore al iniciar la aplicación...");

        TextReader textReader = new TextReader(fileResource);
        textReader.getCustomMetadata().put("filename", "sample.txt");
        List<Document> documentsText = textReader.get();

        List<Document> documents = new TokenTextSplitter().apply(documentsText);

        vectorStore.add(documents);
        log.info("Carga de datos completa. Total de documentos: {}", documents.size());
    }

    // Método para que el controlador obtenga el VectorStore ya cargado
    public VectorStore getVectorStore() {
        return this.vectorStore;
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
