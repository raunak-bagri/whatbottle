package com.whatbottle.service.Impl;


import com.google.gson.Gson;
import com.lithium.mineraloil.api.lia.api.v1.models.BoardV1Response;
import com.lithium.mineraloil.api.lia.api.v2.models.MessageV2Response;
import com.whatbottle.data.Requests.MessageRequest;
import com.whatbottle.data.Requests.WhatsAppMessage;
import com.whatbottle.data.models.Answers;
import com.whatbottle.data.models.AskAQuestionResponse;
import com.whatbottle.data.models.ReplyMessageRequest;
import com.whatbottle.data.models.TopicMuteStatus;
import com.whatbottle.data.pojos.Questions;
import com.whatbottle.repository.ReplyMessageRequestRepository;
import com.whatbottle.repository.TopicMuteStatusRepository;
import com.whatbottle.repository.TopicMuteStatusRepositoryCustom;
import com.whatbottle.service.Whatbottleservice;
import com.whatbottle.util.Constants;
import com.whatbottle.util.PostMesageToLIA;
import com.whatbottle.util.WhatbottleHelper;
import com.whatbottle.util.WhatbottleUtils;
import io.smooch.client.auth.ApiKeyAuth;
import io.smooch.client.model.Enums;
import io.smooch.client.model.Message;
import io.smooch.client.model.MessagePost;
import io.smooch.client.model.MessageResponse;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class WhatbottleserviceImpl implements Whatbottleservice {

    @Autowired
    WhatbottleHelper whatbottleHelper;

    @Autowired
    ReplyMessageRequestRepository replyMessageRequestRepository;

    @Autowired
    TopicMuteStatusRepository topicMuteStatusRepository;

    @Autowired
    PostMesageToLIA postAMessageToLia;

    @Autowired
    TopicMuteStatusRepositoryCustom topicMuteStatusRepositoryCustom;

    private boolean chatEnabled = false;
    private Questions currentQuestion = Questions.START;


    @Override
    public ApiKeyAuth generateToken() throws Exception {
        return whatbottleHelper.generateJWTToken();
    }

    @Override
    public MessageResponse postRepliesMessage(MessageRequest messageRequest, String userId) throws Exception {
        if (chatEnabled) {
            pushMessageToMongo(WhatbottleUtils.MessageRequestToReplyMessageRequestConvertor(messageRequest), userId);
            return successfullyQueuedMessageResponse();
        } else {
            if (topicMuteStatusRepositoryCustom.findActiveTopic().getTopicName().equalsIgnoreCase(messageRequest.getTopicName())) {
                MessagePost messagePost = constructReplyMessage(messageRequest);
                return whatbottleHelper.postAMessage(messagePost, userId);
            } else {
                pushMessageToMongo(WhatbottleUtils.MessageRequestToReplyMessageRequestConvertor(messageRequest), userId);
                return successfullyQueuedMessageResponse();
            }
        }
    }

    @Override
    public MessageResponse postWhatBottleMessage(MessageRequest messageRequest, String userId) throws Exception {
        if (chatEnabled) {
            MessagePost messagePost = constructWhatbottleMessage(messageRequest);
            return whatbottleHelper.postAMessage(messagePost, userId);
        } else {
            throw new Exception("whatbottle-bot not enabled");
        }
    }

    private MessageResponse successfullyQueuedMessageResponse() {
        Message message = new Message();
        message.setText("successfully queued your message. It will be delivered soon");
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage(message);
        return messageResponse;
    }

    private MessageResponse successfullyRepliedMessageResponse() {
        Message message = new Message();
        message.setText("successfully replied in community.");
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage(message);
        return messageResponse;
    }

    @Override
    @Synchronized
    public MessageResponse readAMessage(List<Message> messages) throws Exception {
        String userId = messages.get(0).getAuthorId();
        String name = messages.get(0).getName();
        if (!chatEnabled) {
            if (Constants.greets.contains(messages.get(0).getText().toUpperCase())) {
                currentQuestion = Questions.START;
                chatEnabled = true;
                greetUser(name, userId);
                return postMenu(userId);
            } else {
                postReplyToCommunity(messages.get(0).getText());
                return successfullyRepliedMessageResponse();
            }
        } else {
            return processIncomingMessage(messages.get(0), userId);
        }
    }


    private void greetUser(String name, String userId) throws Exception {
        postWhatBottleMessage(new MessageRequest(String.format(Constants.greetHello, name)), userId);
    }

    private MessageResponse processIncomingMessage(Message message, String userId) throws Exception {
        String text = message.getText();
        if (Constants.greets.contains(message.getText().toUpperCase())) {
            currentQuestion = Questions.START;
        }
        switch (currentQuestion) {
            case START:
                greetUser(message.getName(), userId);
                postMenu(userId);
                break;
            case MENU:
                processMenu(text, userId);
                break;
            case QUESTION:
                fetchAnswer(text, userId);
                break;
            case SATISFIED:
                processSatisfied(text, userId);
                break;
            case UNSATISFIED:
                processUnsatisfied(text, userId);
                break;
            case REITERATE:
                processReiterate(text, userId);
                break;
            case MUTE:
                processTopicMute(text, userId);
                break;
            case UNMUTE:
                processTopicUnMute(text, userId);
                break;

        }
        return new MessageResponse();
    }

    private void postReplyToCommunity(String message) {
        System.out.println("to be done");
    }

    private void processSatisfied(String response, String userId) throws Exception {
        if (response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("y"))
            reiterteMenu(userId);
        else if (response.equalsIgnoreCase("no") || response.equalsIgnoreCase("n"))
            askToPostInCommunity(userId);
        else
            postInvalid(userId);
    }

    private void reiterteMenu(String userId) throws Exception {
        postWhatBottleMessage(new MessageRequest(Constants.iterateQuestion), userId);
        currentQuestion = Questions.REITERATE;
    }

    private void enterCorrectInitialization(String userId) throws Exception {
        postWhatBottleMessage(new MessageRequest(Constants.start), userId);
        currentQuestion = Questions.START;
    }

    private void processReiterate(String response, String userId) throws Exception {
        if (response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("Y"))
            postMenu(userId);
        else if (response.equalsIgnoreCase("no") || response.equalsIgnoreCase("n"))
            terminateConversation(userId);
        else
            postInvalid(userId);
    }

    private void processUnsatisfied(String response, String userId) throws Exception {
        if (response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("Y")) {
            postAMessageToLia.postToCommunityWithNewTopic(response);
            postWhatBottleMessage(new MessageRequest(Constants.postSuccessfulMessage), userId);
            reiterteMenu(userId);
        } else if (response.equalsIgnoreCase("no") || response.equalsIgnoreCase("n"))
            reiterteMenu(userId);
        else
            postInvalid(userId);
    }

    private void askToPostInCommunity(String userId) throws Exception {
        postWhatBottleMessage(new MessageRequest(Constants.answerUnsatisfiedQuestion), userId);
        currentQuestion = Questions.UNSATISFIED;
    }

    private void terminateConversation(String userId) throws Exception {
        postWhatBottleMessage(new MessageRequest(Constants.helpMessage), userId);
        chatEnabled = false;
        processMongoMessages();
    }

    private void postInvalid(String userId) throws Exception {
        postWhatBottleMessage(new MessageRequest(Constants.invalidMessage), userId);
        reiterteMenu(userId);
    }

    private void processMenu(String response, String userId) throws Exception {
        switch (response) {
            case "1":
                askQuestion(userId);
                break;
            case "2":
                showTendingTopics(userId);
                break;
            case "3":
                postUnansweredQuestion(userId);
                break;
            case "4":
                muteTopic(userId);
                break;
            case "5":
                unMuteTopic(userId);
                break;
            case "6":
                terminateConversation(userId);
                break;
            default:
                postInvalid(userId);
        }
    }

    private String fetchAllTopicWithStatuses() {
        String result = "";
        String value;
        List<TopicMuteStatus> topicMuteStatuses = topicMuteStatusRepository.findAll();
        for (TopicMuteStatus topicMuteStatus : topicMuteStatuses) {
            if (topicMuteStatus.getMuteStatus())
                value = Constants.greenHeart;
            else
                value = Constants.redHeart;
            result = result.concat(topicMuteStatus.getTopicId() + " : " + value + "\n");
        }
        return result;
    }

    private void processTopicMute(String text, String userId) throws Exception {
        muteTopic(topicMuteStatusRepository.findOne(text), userId);
        postWhatBottleMessage(new MessageRequest(String.format(Constants.updatedTopicMute, fetchAllTopicWithStatuses())), userId);
        reiterteMenu(userId);
    }

    private void processTopicUnMute(String text, String userId) throws Exception {
        unMuteTopic(topicMuteStatusRepository.findOne(text), userId);
        postWhatBottleMessage(new MessageRequest(String.format(Constants.updatedTopicMute, fetchAllTopicWithStatuses())), userId);
        reiterteMenu(userId);
    }

    private void muteTopic(String userId) throws Exception {
        currentQuestion = Questions.MUTE;
        postWhatBottleMessage(new MessageRequest(String.format(Constants.mute, fetchAllTopicWithStatuses())), userId);
    }

    private void unMuteTopic(String userId) throws Exception {
        currentQuestion = Questions.UNMUTE;
        postWhatBottleMessage(new MessageRequest(String.format(Constants.unMute, fetchAllTopicWithStatuses())), userId);

    }

    private void printInvalidTopicMessage(String userId) throws Exception {
        postWhatBottleMessage(new MessageRequest(Constants.invalidTopicId), userId);
    }

    private TopicMuteStatus muteTopic(TopicMuteStatus topicMuteStatus, String userId) throws Exception {
        try {
            topicMuteStatusRepository.findOne(topicMuteStatus.getTopicId()).getMuteStatus();
        } catch (NullPointerException e) {
            printInvalidTopicMessage(userId);
            return topicMuteStatus;
        }
        return topicMuteStatusRepositoryCustom.updateMuteStatus(topicMuteStatus.getTopicId(), false);
    }

    private TopicMuteStatus unMuteTopic(TopicMuteStatus topicMuteStatus, String userId) throws Exception {
        try {
            topicMuteStatusRepository.findOne(topicMuteStatus.getTopicId()).getMuteStatus();
        } catch (NullPointerException e) {
            printInvalidTopicMessage(userId);
            return topicMuteStatus;

        }
        if (!topicMuteStatusRepository.findOne(topicMuteStatus.getTopicId()).getMuteStatus()) { //if topic is false
            try {
                topicMuteStatusRepositoryCustom.updateMuteStatus(topicMuteStatusRepositoryCustom.findActiveTopic().getTopicId(), false);
            } catch (NullPointerException e) {
                log.info("No topic is unmuted");
            }
        }
        return topicMuteStatusRepositoryCustom.updateMuteStatus(topicMuteStatus.getTopicId(), true);
    }

    public TopicMuteStatus insertTopicMuteStatus(TopicMuteStatus topicMuteStatus) {
        return topicMuteStatusRepository.save(topicMuteStatus);
    }

    private void postUnansweredQuestion(String userId) throws Exception {
        postWhatBottleMessage(new MessageRequest(Constants.showUnansweredMessages), userId);
        reiterteMenu(userId);
    }

    private MessagePost constructReplyMessage(MessageRequest messageRequest) throws Exception {
        MessagePost messagePost = new MessagePost();
        messagePost.setRole("appMaker");
        messagePost.setType(Enums.MessageTypeEnum.TEXT.toString());
        messagePost.setText("*" + messageRequest.getTopicName() + "*\n" + "_" + messageRequest.getUserName() + "_" + " : " + messageRequest.getMessage());
        return messagePost;
    }

    private MessagePost constructWhatbottleMessage(MessageRequest messageRequest) throws Exception {
        MessagePost messagePost = new MessagePost();
        messagePost.setRole("appMaker");
        messagePost.setType(Enums.MessageTypeEnum.TEXT.toString());
        messagePost.setText(messageRequest.getMessage());
        return messagePost;
    }

    private MessageResponse postMenu(String userId) throws Exception {
        MessageRequest messageRequest = new MessageRequest(Constants.menu);
        MessageResponse messageResponse = postWhatBottleMessage(messageRequest, userId);
        currentQuestion = Questions.MENU;
        return messageResponse;
    }

    private void showTendingTopics(String userId) throws Exception {
        postWhatBottleMessage(new MessageRequest(Constants.showTrendingTopics), userId);
        reiterteMenu(userId);
    }

    //Hack
    private void fetchAnswer(String question, String userId) throws Exception {
        Answers fetchedAswers = Constants.questions.get(question.toUpperCase());
        if (Objects.isNull(fetchedAswers)) {
            askToPostInCommunity(userId);
            currentQuestion = Questions.UNSATISFIED;
        } else {
            //postFetchedAnswersToWhatBottle(fetchedAswers, userId);
            currentQuestion = Questions.SATISFIED;
            postWhatBottleMessage(new MessageRequest(fetchedAswers.getAnswer()+"\n\n"+fetchedAswers.getUrl()), userId);
            postWhatBottleMessage(new MessageRequest(Constants.answerSatisfiedQuestion), userId);
        }
    }

    private ReplyMessageRequest pushMessageToMongo(ReplyMessageRequest replyMessageRequest, String userId) {
        return replyMessageRequestRepository.save(replyMessageRequest);
    }

    private void processMongoMessages() throws Exception {
        String activeTopicId = topicMuteStatusRepositoryCustom.findActiveTopic().getTopicName();
        List<ReplyMessageRequest> replyMessageRequestsAll = replyMessageRequestRepository.findAll();
        for (ReplyMessageRequest replyMessageRequest : replyMessageRequestsAll) {
            if (replyMessageRequest.getTopicName().equalsIgnoreCase(activeTopicId)) {
                postRepliesMessage(WhatbottleUtils.ReplyMessageRequestToMessageRequestConvertor(replyMessageRequest), replyMessageRequest.getUserId());
                replyMessageRequestRepository.delete(replyMessageRequest);
            } else {

            }
        }
    }

    private void askQuestion(String userId) throws Exception {
        postWhatBottleMessage(new MessageRequest(Constants.questionMessageQuestion), userId);
        currentQuestion = Questions.QUESTION;
    }

    private void postFetchedAnswersToWhatBottle(List<AskAQuestionResponse> askAQuestionResponses, String userId) throws Exception {
        int counter = 0;
        String answer = "";
        for (AskAQuestionResponse askAQuestionResponse : askAQuestionResponses) {
            answer = answer + Integer.toString(counter++) + "_" + askAQuestionResponse.getMessage() + "_" + "\n" +
                    askAQuestionResponse.getUrl() + "\n";
        }
        postWhatBottleMessage(new MessageRequest(answer), userId);

    }

    @Override
    public MessageResponse replyToTopic(WhatsAppMessage whatsAppMessage) throws Exception {
        BoardV1Response m = postAMessageToLia.replyToTopic(whatsAppMessage);
        Message message = new Message();
        message.setText(new Gson().toJson(m));
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage(message);
        return messageResponse;
    }

}
