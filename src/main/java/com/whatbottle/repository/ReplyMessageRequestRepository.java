package com.whatbottle.repository;


import com.whatbottle.data.models.ReplyMessageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 *
 * @author raunak.bagri
 *
 */
@Repository
public interface ReplyMessageRequestRepository extends MongoRepository<ReplyMessageRequest, String> {

    @Override
    ReplyMessageRequest save(ReplyMessageRequest replyMessageRequest);

    List<ReplyMessageRequest> findAllOrOrderByCreatedDate();

    @Override
    void delete(ReplyMessageRequest replyMessageRequest);

}
