package com.dova.dev.http;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuzhendong on 16/5/14.
 */
public class HttpExample {

    private HttpClient client = HttpClientFactory.get();

    @Test
    public void get(){
        String url = "http://www.baidu.com";
        HttpUriRequest request = RequestBuilder.get(url).setConfig(HttpClientFactory.getDefaultRequestConfig())
                .build();
        HttpResponse response = null;
        HttpEntity entity = null;
        try {
            response = client.execute(request);
            entity = response.getEntity();
            System.out.println("response:" + response.toString());
            String text = EntityUtils.toString(entity);
            System.out.println(String.format("%s %s",entity.getClass(),text));
        }catch (IOException e){

        }finally {
            if(entity != null){
                //关闭inputstream的链接
                EntityUtils.consumeQuietly(entity);
            }
        }
    }

    @Test
    public void postAsForm(){
        String url = "http://www.futureinst.com/api";
        List<NameValuePair> data = new ArrayList<NameValuePair>();
        String query="{\"event_id\":399,\"scope\":\"price\"}";
        data.add(new BasicNameValuePair("type","event"));
        data.add(new BasicNameValuePair("method","query_single_event"));
        data.add(new BasicNameValuePair("query", query));
        HttpResponse response = null;
        HttpEntity responseEntity = null;
        try{
            HttpEntity requestEntity = new UrlEncodedFormEntity(data, "UTF-8");
            HttpUriRequest request = RequestBuilder.post(url)
                    .setConfig(HttpClientFactory.getDefaultRequestConfig())
                    .setEntity(requestEntity)
                    .build();
            response = client.execute(request);
            responseEntity = response.getEntity();
            //System.out.println("response:" + response.toString());
            String text = EntityUtils.toString(responseEntity);
            System.out.println(String.format("%s\n%s",responseEntity.getClass(),text));
        }catch (UnsupportedEncodingException ne){

        }catch (IOException e){

        }finally {
            if(responseEntity != null) {
                //关闭inputstream的链接
                EntityUtils.consumeQuietly(responseEntity);
            }
        }
    }
}
