package com.whatbottle.repository.Impl;

import com.whatbottle.data.models.TopicMuteStatus;
import com.whatbottle.repository.TopicMuteStatusRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class TopicMuteStatusRepositoryCustomImpl implements TopicMuteStatusRepositoryCustom {


    @Autowired
    MongoOperations mongoOperations;

    @Override
    public TopicMuteStatus updateMuteStatus(String topicId, boolean muteStatus) {
        Criteria topicIdCriteria = Criteria.where("topicId").is(topicId);
        Query query = new Query();
        query.addCriteria(topicIdCriteria);
        Update update = new Update();
        update.set("muteStatus", muteStatus);
        return mongoOperations.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), TopicMuteStatus.class);
    }

    @Override
    public TopicMuteStatus findActiveTopic() {
        Criteria topicIdCriteria = Criteria.where("muteStatus").is(true);
        Query query = new Query();
        query.addCriteria(topicIdCriteria);
        return mongoOperations.findOne(query,TopicMuteStatus.class);
    }
}
