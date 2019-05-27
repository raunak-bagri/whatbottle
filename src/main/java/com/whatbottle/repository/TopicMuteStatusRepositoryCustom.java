package com.whatbottle.repository;

import com.whatbottle.data.models.TopicMuteStatus;

public interface TopicMuteStatusRepositoryCustom {

    public TopicMuteStatus updateMuteStatus(String topicId, boolean muteStatus);

    public TopicMuteStatus findActiveTopic();


}
