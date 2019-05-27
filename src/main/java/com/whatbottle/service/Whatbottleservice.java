package com.whatbottle.service;

import com.whatbottle.data.Requests.MessageRequest;
import com.whatbottle.data.Requests.WhatsAppMessage;
import com.whatbottle.data.models.TopicMuteStatus;
import io.smooch.client.auth.ApiKeyAuth;
import io.smooch.client.model.Message;
import io.smooch.client.model.MessageResponse;

import java.util.List;

public interface Whatbottleservice {
    ApiKeyAuth generateToken() throws Exception;

    MessageResponse postRepliesMessage(MessageRequest messageRequest, String userId) throws  Exception;

    MessageResponse postWhatBottleMessage(MessageRequest messageRequest, String userId) throws Exception;

    MessageResponse readAMessage(List<Message> messages)  throws Exception;


    MessageResponse replyToTopic(WhatsAppMessage whatsAppMessage)throws Exception;

    TopicMuteStatus insertTopicMuteStatus(TopicMuteStatus topicMuteStatus);

    }


