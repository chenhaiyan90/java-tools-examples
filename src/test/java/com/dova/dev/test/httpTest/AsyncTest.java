package com.dova.dev.test.httpTest;

import com.dova.dev.async_http.AsyncRequestProducer;
import com.dova.dev.async_http.AsyncResponseConsumer;
import com.dova.dev.async_http.HttpClientFactory;
import com.dova.dev.async_http.Response;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.nio.client.HttpAsyncClient;
import org.junit.Test;

import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by liuzhendong on 16/6/29.
 */
public class AsyncTest {

    HttpAsyncClient asyncClient = HttpClientFactory.getAsyncClient();

    @Test
    public void testAsync()throws Exception{
        HttpHost httpHost = HttpHost.create("http://localhost:8081");
        String seq  = UUID.randomUUID().toString();
        HttpUriRequest request = RequestBuilder.get("/dishes/4835").build();
        Future<Response> responseFuture = asyncClient.execute(new AsyncRequestProducer(seq,httpHost,request),
                new AsyncResponseConsumer(seq),null);
        System.out.println(responseFuture.get(3, TimeUnit.SECONDS));
        Thread.sleep(20 * 1000);
    }
}
