package com.whatbottle.data.pojos;

import com.whatbottle.util.Constants;
import org.apache.commons.lang3.StringUtils;

public enum Questions {
    INITIATE(StringUtils.join(Constants.greets,",")),
    UNSATISFIED(Constants.answerUnsatisfiedQuestion),
    SATISFIED(Constants.answerSatisfiedQuestion),
    MENU(String.join(",",Constants.menu)),
    REITERATE(Constants.iterateQuestion),
    QUESTION(Constants.questionMessageQuestion),
    START(Constants.start),
    MUTE(Constants.mute),
    UNMUTE(Constants.unMute);

    Questions(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    private String question;
}
