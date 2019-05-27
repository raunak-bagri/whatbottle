package com.whatbottle.data.models;

import lombok.Data;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.io.IOException;

@Data
public class SendHttp {
    private PoolingHttpClientConnectionManager cm;
    private CloseableHttpClient httpClient;
    private RequestConfig requestConfig;
    public SendHttp(int maxConnections, int requestTimeOut, int socketTimeout) {
        cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(maxConnections);
        cm.setDefaultMaxPerRoute(maxConnections);
        httpClient = HttpClients.custom().setConnectionManager(cm).build();
        requestConfig = RequestConfig.custom().setConnectionRequestTimeout(requestTimeOut)
                .setSocketTimeout(socketTimeout).build();
    }
    public CloseableHttpResponse send(HttpEntityEnclosingRequestBase request) throws IOException {
        request.setConfig(requestConfig);
        return httpClient.execute(request);
    }
}