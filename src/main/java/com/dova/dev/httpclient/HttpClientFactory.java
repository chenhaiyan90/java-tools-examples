package com.dova.dev.async_http;


import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.protocol.HttpContext;

public class HttpClientFactory {
    static RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(3000)
            .setConnectTimeout(3000).build();

    private static CloseableHttpAsyncClient asyncClient;
    private static HttpClient defaultClient;

    public static synchronized CloseableHttpAsyncClient getAsyncClient() {
        if (asyncClient == null) {
            asyncClient = HttpAsyncClients.custom()
                    .setDefaultRequestConfig(requestConfig)
                    .setUserAgent("HomeCooked Trade HTTP Client")
                    .setMaxConnPerRoute(32)
                    .setMaxConnTotal(256)
                    .build();
            asyncClient.start();
        }

        return asyncClient;
    }


    public synchronized static HttpClient get() {
        if (defaultClient == null) {
            defaultClient = HttpClientBuilder.create()
                    .setUserAgent("Home-Cooked Order HTTP Client")
                    .setRetryHandler(new StandardHttpRequestRetryHandler(3, false))
                    .setServiceUnavailableRetryStrategy(new DefaultServiceUnavailableRetryStrategy())
                    .setMaxConnPerRoute(32)
                    .setMaxConnTotal(256)
                    .build();
        }
        return defaultClient;
    }

    static class DefaultServiceUnavailableRetryStrategy implements ServiceUnavailableRetryStrategy {
        public final static int MAX_RETRIES    = 3;
        public final static int RETRY_INTERVAL = 1000;

        @Override
        public boolean retryRequest(HttpResponse response, int executionCount, HttpContext context) {
            return executionCount <= MAX_RETRIES &&
                    response.getStatusLine().getStatusCode() >= HttpStatus.SC_INTERNAL_SERVER_ERROR;
        }

        @Override
        public long getRetryInterval() {
            return RETRY_INTERVAL;
        }
    }

}
