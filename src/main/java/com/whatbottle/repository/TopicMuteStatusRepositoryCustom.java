package com.whatbottle.repository;

import com.whatbottle.data.models.TopicMuteStatus;

public interface TopicMuteStatusRepositoryCustom {

    TopicMuteStatus updateMuteStatus(String topicId, boolean muteStatus);

    TopicMuteStatus findActiveTopic();


}
