package com.whatbottle.data.models;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


/**
 *
 * @author gunaas
 *
 */

@Document(collection = "ReplyMessageRequestQueue")
@Data
public class ReplyMessageRequest {
    @Id
    String id;

    String message;

    String userName;

    String topicName;

    String userId;

    Date createdDate = new Date();

    public ReplyMessageRequest(String message) {
        this.message = message;
    }

    public ReplyMessageRequest() {
    }
}
