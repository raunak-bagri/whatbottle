package com.whatbottle.service;

import com.whatbottle.data.Requests.MessageRequest;
import com.whatbottle.data.Requests.WhatsAppMessage;
import com.whatbottle.data.models.TopicMuteStatus;
import io.smooch.client.auth.ApiKeyAuth;
import io.smooch.client.model.Message;
import io.smooch.client.model.MessageResponse;

import java.util.List;

public interface Whatbottleservice {
    public ApiKeyAuth generateToken() throws Exception;

    public MessageResponse postRepliesMessage(MessageRequest messageRequest, String userId) throws  Exception;

    public MessageResponse postWhatBottleMessage(MessageRequest messageRequest, String userId) throws Exception;

    public MessageResponse readAMessage(List<Message> messages)  throws Exception;


    public MessageResponse replyToTopic(WhatsAppMessage whatsAppMessage)throws Exception;

    public TopicMuteStatus insertTopicMuteStatus(TopicMuteStatus topicMuteStatus);

    }


