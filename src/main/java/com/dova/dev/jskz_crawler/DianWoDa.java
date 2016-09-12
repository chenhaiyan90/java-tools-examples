package com.dova.dev.jskz_crawler;

import com.dova.dev.async_http.HttpClientFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

/**
 * Created by liuzhendong on 16/9/8.
 */
public class DianWoDa {

    HttpClient httpClient = HttpClientFactory.get();
    @Test
    public void viewOrderList()throws Exception{
        String host = "flash3.dianwoda.cn";
        HttpUriRequest request = RequestBuilder.post("http://" + host + "/v17/rider/view-order-list.json" +
                "?riderId=226511" +
                "&lat=39997612" +
                "&lng=116482541" +
                "&cityId=12" +
                "&sortType=0" +
                "&checkRiderId=226511" +
                "&cityName=%E5%8C%97%E4%BA%AC%E5%B8%82" +
                "&token=5efe635f-f373-461a-a8b2-fb02e59cfd10" +
                "&sign=30d451ec8cad3836086639937f7a267b")
                .addHeader("Accept", "application/json")
                .addHeader("Accept-Encoding","gzip")
                .addHeader("Accept-Language","zh-Hans;q=1")
                .addHeader("Content-Type", "application/json;charset=UTF-8")
                .addHeader("Client-Agent", "3.6.0")
                .addHeader("Content-Encoding", "gzip")
                .addHeader("Content-Length","0")
                .addHeader("Connection", "keep-alive")
                .addHeader("User-Agent", "Rider/3.6.0 (iPhone; iOS 8.2; Scale/2.00)").build();
        HttpResponse response = httpClient.execute(request);
        System.out.println(response.getStatusLine().getStatusCode());
        System.out.println(EntityUtils.toString(response.getEntity()));
    }
}
