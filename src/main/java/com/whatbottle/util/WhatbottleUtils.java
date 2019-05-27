package com.whatbottle.util;


import com.whatbottle.data.Requests.MessageRequest;
import com.whatbottle.data.models.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Date;


/**
 * Created by guna on 30/06/18.
 */
@Slf4j
public class WhatbottleUtils {



    public static ReplyMessageRequest MessageRequestToReplyMessageRequestConvertor(MessageRequest messageRequest) {
        ReplyMessageRequest replyMessageRequest = new ReplyMessageRequest();
        replyMessageRequest.setCreatedDate(new Date());
        replyMessageRequest.setMessage(messageRequest.getMessage());
        replyMessageRequest.setTopicName(messageRequest.getTopicName());
        replyMessageRequest.setUserName(messageRequest.getUserName());
        replyMessageRequest.setUserId(messageRequest.getUserId());
        return replyMessageRequest;
    }

    public static MessageRequest ReplyMessageRequestToMessageRequestConvertor(ReplyMessageRequest replyMessageRequest) {
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setId(replyMessageRequest.getId());
        messageRequest.setCreatedDate(replyMessageRequest.getCreatedDate());
        messageRequest.setMessage(replyMessageRequest.getMessage());
        messageRequest.setTopicName(replyMessageRequest.getTopicName());
        messageRequest.setUserName(replyMessageRequest.getUserName());
        messageRequest.setUserId(replyMessageRequest.getUserId());
        return messageRequest;
    }

    public static TriggerResponse triggerHttpCall(String jsonBody,String url) {
        TriggerResponse triggerResponse = new TriggerResponse();
        log.info("-json to post-" + jsonBody);
        log.info("call back url is : " + url);
        HttpPost request = getPostRequest(url);
        HttpRequester requester = HttpRequester.getHttpRequester();
        try {
            request.setEntity(new StringEntity(jsonBody));
            try (CloseableHttpResponse response = requester.getHttp().send(
                    request)) {
                int statusCode = response.getStatusLine().getStatusCode();
                String message = EntityUtils.toString(response.getEntity());
                triggerResponse.setStatus(statusCode);
                triggerResponse.setMessage(message);
                log.info("-responseJson-" + triggerResponse.toString());
                log.info("Http call done : " + " statusCode - " + statusCode + " message - " + message);

            } catch (Exception e) {
                log.error("unable to do Http call ");
            }
        } catch (UnsupportedEncodingException e) {
            log.error("Error in parsing triggerResponse ");
        }
        return triggerResponse;
    }


    public static HttpPost getPostRequest(String url) {

        HttpPost request = new HttpPost();
        request.setURI(getUri(url));
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");
        return request;
    }

    private static URI getUri(String url) {
        return URI.create(url);
    }


}