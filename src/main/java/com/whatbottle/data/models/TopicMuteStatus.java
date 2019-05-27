package com.whatbottle.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "MutedTopics")
public class TopicMuteStatus {
    @Id
    private String topicId;

    private String topicName;
    private Boolean muteStatus;
}
