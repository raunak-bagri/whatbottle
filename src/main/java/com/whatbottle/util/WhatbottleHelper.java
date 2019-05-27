package com.whatbottle.util;

import com.google.gson.Gson;
import com.whatbottle.data.models.AskAQuestionRequest;
import com.whatbottle.data.models.AskAQuestionResponse;
import com.whatbottle.data.models.ResponseModel;
import com.whatbottle.data.models.TriggerResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.smooch.client.ApiClient;
import io.smooch.client.ApiException;
import io.smooch.client.Configuration;
import io.smooch.client.api.ConversationApi;
import io.smooch.client.auth.ApiKeyAuth;
import io.smooch.client.model.MessagePost;
import io.smooch.client.model.MessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;

import static io.jsonwebtoken.JwsHeader.KEY_ID;


/**
 *
 * @author gunaas
 *
 */

@Slf4j
@Component
public class WhatbottleHelper {

    private static ApiClient defaultApiClient = Configuration.getDefaultApiClient();
    private static ApiKeyAuth jwt = (ApiKeyAuth) defaultApiClient.getAuthentication("jwt");
    @Autowired
    RestTemplate restTemplate;

    @Value("${appId}")
    private String APP_ID;

    @Value("${appUserId}")
    private String APP_USER_ID;

    @Value("${webhookId}")
    private String WEBHOOK_ID;

    @Value("${appKeyId}")
    private String KEY_ID_VALUE;

    @Value("${appKeySecret}")
    private String KEY_SECRET_VALUE;

    @Value("${AskAQuestionServiceUrl}")
    private String ASK_A_QUESTION_SERVICE_URL;

    @PostConstruct
    private void initializeToken() {
        String token = Jwts.builder()
                .claim("scope", "app")
                .setHeaderParam(KEY_ID, KEY_ID_VALUE)
                .signWith(
                        Keys.hmacShaKeyFor(KEY_SECRET_VALUE.getBytes()),
                        SignatureAlgorithm.HS256
                )
                .compact();
        jwt.setApiKey(token);
        jwt.setApiKeyPrefix("Bearer");
    }

    public ApiKeyAuth generateJWTToken() {
        String token = Jwts.builder()
                .claim("scope", "app")
                .setHeaderParam(KEY_ID, KEY_ID_VALUE)
                .signWith(
                        Keys.hmacShaKeyFor(KEY_SECRET_VALUE.getBytes()),
                        SignatureAlgorithm.HS256
                )
                .compact();
        jwt.setApiKey(token);
        jwt.setApiKeyPrefix("Bearer");
        return jwt;
    }

    public MessageResponse postAMessage(MessagePost messagePostBody, String userId) {
        if (StringUtils.isBlank(userId) || StringUtils.isEmpty(userId)) {
            userId = APP_USER_ID;
        }
        ConversationApi apiInstance = new ConversationApi();
        MessageResponse result = new MessageResponse();// MessagePost | Body for a postMessage request. Additional arguments are necessary based on message type ([text](https://docs.smooch.io/rest#text-message), [image](https://docs.smooch.io/rest#image-message), [carousel](https://docs.smooch.io/rest#carousel-message), [list](https://docs.smooch.io/rest#list-message))
        try {
            result = apiInstance.postMessage(APP_ID, userId,  messagePostBody);
            log.info("Successfully posted -> response: " + result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ConversationApi #postMessage");
            e.printStackTrace();
        }
        return result;
    }


    public List<AskAQuestionResponse> fetchAnswerFromAskAQuestionService(String question) {
//        AskAQuestionRequest askAQuestionRequest = new AskAQuestionRequest(question);
//        Gson gson = new Gson();
//        String json =  gson.toJson(askAQuestionRequest);
//        TriggerResponse triggerResponse = WhatbottleUtils.triggerHttpCall(json,ASK_A_QUESTION_SERVICE_URL);
//        JSONArray jsonArray = new JSONArray(triggerResponse.getMessage());
//
//        returnObject = gson.fromJson(jsonArray.get(0).toString(),clazz);

        return new ArrayList<>();
    }

}
