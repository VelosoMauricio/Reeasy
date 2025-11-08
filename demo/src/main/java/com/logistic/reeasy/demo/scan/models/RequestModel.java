package com.logistic.reeasy.demo.scan.models;
import lombok.Data;
import java.util.List;

@Data
public class RequestModel {
    private String model;
    private String prompt;
    private List<String> images; //
    //@JsonProperty("stream")
    private boolean stream; //

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