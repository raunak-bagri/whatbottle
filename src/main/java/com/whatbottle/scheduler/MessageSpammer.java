package com.whatbottle.scheduler;


import com.whatbottle.service.Impl.WhatbottleserviceImpl;
import com.whatbottle.service.Whatbottleservice;
import com.whatbottle.util.WhatbottleHelper;
import io.smooch.client.model.Enums;
import io.smooch.client.model.MessagePost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class MessageSpammer {

    @Autowired
    WhatbottleHelper whatbottleHelper;

//    @Scheduled(fixedDelay = 1000)
//    public void scheduledMethod() {
//        MessagePost messagePost = new MessagePost();
//        messagePost.setRole("appMaker");
//        messagePost.setType(Enums.MessageTypeEnum.TEXT.toString());
//        messagePost.setText("Sorry! I spammed you enough! bubye :)");
//        System.out.println("Sorry! I spammed you enough! bubye :)");
//        whatbottleHelper.postAMessage(messagePost, "");
//    }

/* Do not uncomment unless you want to spam somebody */
}
