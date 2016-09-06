package com.dova.dev.jskz_crawler;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author luhaijun@jiashuangkuaizi.com
 * @ClassName: SendCoupon
 * @Description: 描述
 * @date 2016/8/8 20:01
 */
public class PrepareCoupon {

    private CloseableHttpClient httpClient;

    private ExecutorService executorService = Executors.newFixedThreadPool(200);

    public PrepareCoupon() {
        PoolingHttpClientConnectionManager pcm = new PoolingHttpClientConnectionManager();
        pcm.setMaxTotal(4096);
        pcm.setDefaultMaxPerRoute(1024);
        httpClient = HttpClients.custom().setConnectionManager(pcm).setDefaultRequestConfig(getRequestConfig()).build();
    }

    private RequestConfig getRequestConfig() {
        return RequestConfig.custom()
                .setConnectionRequestTimeout(3000000)
                .setConnectTimeout(3000000)
                .setSocketTimeout(3000000).build();
    }

    public List<String> readFileByLines(String fileName) {
        List<String> list = new ArrayList<String>();
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                list.add(tempString.trim());
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return list;
    }


    public void sendCoupon(String phones) throws Exception {
        HttpPost post = new HttpPost("http://m.jiashuangkuaizi.com/api/V201607/GiveOutNewsUserCouponPre");
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("userPhones", phones));
        list.add(new BasicNameValuePair("key", "578f654ee3438"));
        list.add(new BasicNameValuePair("type", "19"));
        post.setEntity(new UrlEncodedFormEntity(list, "utf-8"));
        CloseableHttpResponse response = httpClient.execute(post);
        if (response.getStatusLine().getStatusCode() / 100 == 2) {
            String ret = EntityUtils.toString(response.getEntity());
            System.out.println(ret);
        } else {
            throw new Exception("接口调用失败");
        }
    }


    public static void main(String[] arg) {
        final PrepareCoupon sendCoupon = new PrepareCoupon();
        List<String> list = sendCoupon.readFileByLines("/Users/liuzhendong/Documents/jskz_crawler/build/0905/fengniao.phones.all");
        int i = 0;
        String phones = "";
        Semaphore semaphore = new Semaphore(0);
        int signNum = 0;
        for (String phone : list) {
            if (i == 100) {
                phones += phone;
                i = 0;
                signNum++;
                final String temp = phones;
                final int tmpSignNum = signNum;
                System.out.println("send_start:" + signNum);
                sendCoupon.executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            sendCoupon.sendCoupon(temp);
                            System.out.println("send_end:" + tmpSignNum);
                            semaphore.release(1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                phones = "";
            } else {
                phones += phone + ",";
                i++;
            }
        }
        try {
            semaphore.acquire(signNum);
            sendCoupon.executorService.shutdown();
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("总共发了："+list.size());
    }
}
