package com.whatbottle.data.Requests;

import lombok.Data;

@Data
public class WhatsAppMessage {
    private String id;
    private String lia_id;
    private String parent_id;
    private String messageBody;
    private String boardName;
    private String author;
}
