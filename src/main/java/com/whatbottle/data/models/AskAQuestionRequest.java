package com.whatbottle.data.models;


import lombok.Data;

@Data
public class AskAQuestionRequest {
    String question;

    public AskAQuestionRequest(String question) {
        this.question = question;
    }
}
