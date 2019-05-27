package com.whatbottle.util;

import com.google.gson.Gson;
import com.whatbottle.data.models.Data;
import com.whatbottle.data.models.Paragraphs;
import com.whatbottle.data.models.Qas;
import com.whatbottle.data.models.SummarizerRequest;
import com.whatbottle.data.models.SummarizerResponse;
import com.whatbottle.data.models.TriggerResponse;
import org.springframework.beans.factory.annotation.Value;

public class SummarizerConnector {

    @Value("${summarizerUrl}")
    private String url;

    private String fetchSummarizedText(String question,String answer){
        return createSummarizerRequest(question,answer).getCondensed();
    }

    private SummarizerResponse createSummarizerRequest(String question, String answer){
        Qas[] qas = new Qas[1];
        qas[0] = new Qas();
        qas[0].setQuestion(question);
        Paragraphs[] paragraphs = new Paragraphs[1];
        paragraphs[0] =new Paragraphs();
        paragraphs[0].setQas(qas);
        paragraphs[0].setContext(answer);
        Data[] data = new Data[1];
        data[0] = new Data();
        data[0].setParagraphs(paragraphs);
        SummarizerRequest summarizerRequest = new SummarizerRequest();
        summarizerRequest.setData(data);

        Gson gson = new Gson();
        String request = gson.toJson(summarizerRequest);
        TriggerResponse triggerResponse = WhatbottleUtils.triggerHttpCall(request,url);
        return gson.fromJson(triggerResponse.getMessage(),SummarizerResponse.class);
    }

}
