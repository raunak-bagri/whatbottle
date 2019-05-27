package com.whatbottle.util;


import com.google.common.collect.ImmutableMap;
import com.whatbottle.data.models.Answers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by gunaas
 */
public interface Constants {

    String whatbottle = "*Whatbottle*\n";

    List<String> greets = Stream.of("HEY WHATBOTTLE","HEY","HELLO","HI").collect(Collectors.toList());
    String start = whatbottle + "Please enter \" *Hey whatbottle*\" to initiate the conversation with Whatbottle!";
    String menu = whatbottle + "*1.* Ask a question\n*2.* Show Trending Topics\n*3.* Unanswered questions \n*4.* Mute a Topic\n*5.* Unmute a Topic\n*6.* Do nothing!\n_Please enter your option_";

    String invalidMessage = whatbottle + "_Oops! Looks like I didn't understand what you want me to do!_";
    String helpMessage = whatbottle + "_Glad to help You! Have a great day ahead_ \uD83D\uDE04";
    String postSuccessfulMessage = whatbottle + "Message Posted!";

    String questionMessageQuestion = whatbottle + "_Please ask your question_";
    String answerUnsatisfiedQuestion = whatbottle + "_Sorry! I couldn't find the answer to this question, Would you like me to post this question on the community?_\n Answer *Y* or *N*";
    String answerSatisfiedQuestion = whatbottle + "_Are you Satisfied with the above answer?_\n Answer *Y* or *N*";
    String iterateQuestion = whatbottle + "_Is there anything else we can help you with?_\n Answer *Y* or *N*";
    String mute = whatbottle +"_Following are the topics and corresponding value:_ \n%s \n_Please choose the topic to be muted._";
    String unMute =whatbottle + "_Following are the topics and corresponding value:_ \n%s\n_Please choose which one to unmute._";
    String updatedTopicMute =whatbottle + "_Following are the topics and corresponding updated value_\n%s\n_(Please note that you can unmute only one topic at a time)_";
    String invalidTopicId =whatbottle + "The topic which you have provided is invalid, Please give the proper topic id";

    String greetHello = whatbottle + "_Hello_ %s";
    String redHeart = "\u2764\uFE0F";
    String greenHeart = "\uD83D\uDC9A";

    String showTrendingTopics=whatbottle + "_Trending now:_ \n 1. _Iphone X black sceen issue_\n2.Samsung Galaxy Note 9 heating issue\n3. pixel 3 photography";

    String showUnansweredMessages = whatbottle +"_Based on your interests, here are a few unanswered questions_\n\n1. Does Galaxy S9 have a heating issue\n2. What are the storage options available for the latest iphone\n3.Does the Pixel 3 support wireless charging";

    Map<String,Answers> questions = ImmutableMap.of("WHATS THE XIAOMI MI A2 COST IN BANGALORE?",new Answers("13,670 INR","http://google.com"),
            "WHICH CAMERAS ARE BEST FOR PHOTOGRAPHY?",new Answers("Iphone Xs ,Pixel 3","http://google.com"),
            "MOST TRENDING DISPLAYS OF ANY PHONE?",new Answers("Samsung s9+, Iphone Xs","http://google.com"), "S9+ NO SOUND WITH NOTIFICATIONS?" , new Answers("This worked:\n\nhttps://www.youtube.com/watch?v=H7fjg90LIsg","http://google.com"));

}
