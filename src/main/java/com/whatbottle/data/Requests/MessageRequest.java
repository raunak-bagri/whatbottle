package com.whatbottle.data.Requests;

import lombok.Data;

import java.util.Date;

@Data
public class MessageRequest {
    String id;

    String message;

    String userName;

    String topicName;

    String userId;

    Date createdDate = new Date();

    public MessageRequest(String message) {
        this.message = message;
    }

    public MessageRequest() {
    }

}
