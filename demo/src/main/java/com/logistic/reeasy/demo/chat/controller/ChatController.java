package com.logistic.reeasy.demo.chat.controller;

import com.logistic.reeasy.demo.chat.service.ChatService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChatController {

    private ChatService chatService;

    public ChatController(ChatService chatService){
        this.chatService = chatService;
    }

    @GetMapping
    public String sendMessage(@RequestParam("message") String message) {
        return "chat";
    }
}
