package com.logistic.reeasy.demo.scan.models;
import lombok.Data;

@Data
public class RequestModel {
    private String model;
    private String prompt;
    private String images;
    private String stream = "false";

}

/* 
{
  "model": "gemma3-vision",
  "prompt": "Describe lo que ves en esta imagen.",
  "images": [
    "/9j/4AAQSkZJRgABAQ... ( Base64) ..."
  ],
  "stream": false
}
*/