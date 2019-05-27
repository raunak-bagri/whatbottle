package com.whatbottle.data.Requests;

import io.smooch.client.model.App;
import io.smooch.client.model.AppUser;
import io.smooch.client.model.Message;
import lombok.Data;

import java.util.List;

@Data
public class WebhookRequest {
    String trigger;
    App app;
    List<Message> messages;
    AppUser appUser;
    String version;

}
