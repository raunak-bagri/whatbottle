package com.whatbottle.data.models;


public class Answers {
    private String answer;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;

    public Answers(String answer, String url) {
        this.answer = answer;
        this.url = url;
    }
}
