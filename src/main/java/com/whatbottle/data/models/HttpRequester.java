package com.whatbottle.data.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;


@Data
public class HttpRequester {

    private SendHttp http;
    private ObjectMapper mapper;

    private static HttpRequester requester;

    private HttpRequester() {
        http = new SendHttp(200, 10000, 15000);
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

    }

    public static synchronized HttpRequester getHttpRequester() {
        if (requester == null) {
            requester = new HttpRequester();
        }
        return requester;
    }


}

