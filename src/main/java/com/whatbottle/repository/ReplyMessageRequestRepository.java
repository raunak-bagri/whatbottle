package com.whatbottle.repository;


import com.whatbottle.data.models.ReplyMessageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 *
 * @author gunaas
 *
 */
@Repository
public interface ReplyMessageRequestRepository extends MongoRepository<ReplyMessageRequest, String> {
    ReplyMessageRequest save(ReplyMessageRequest replyMessageRequest);

    List<ReplyMessageRequest> findAllOrOrderByCreatedDate();

    void  delete(ReplyMessageRequest replyMessageRequest);

}
