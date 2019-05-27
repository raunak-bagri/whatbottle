package com.whatbottle.repository;

import com.whatbottle.data.models.ReplyMessageRequest;
import com.whatbottle.data.models.TopicMuteStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TopicMuteStatusRepository extends MongoRepository<TopicMuteStatus, String> {

    TopicMuteStatus save(TopicMuteStatus replyMessageRequest);

    void delete(TopicMuteStatus topicMuteStatus);

}
