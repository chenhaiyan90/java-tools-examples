package com.dova.dev.async_http;


import com.google.common.base.Preconditions;
import org.apache.http.HttpResponse;

import java.util.Objects;

public class Response {
    public String      sequence;

    public String      body;
    public int          httpCode;

    public static Response of(String sequence,String body, int httpCode) {
        Response response = new Response();
        response.sequence = sequence;
        response.body = body;
        response.httpCode = httpCode;
        return response;
    }


    public boolean ok() {
        return httpCode/100 == 2;
    }


    @Override
    public String toString() {
        return "Response{" +
                "sequence='" + sequence + '\'' +
                ", httpCode=" + httpCode +
                ", body=" + body +
                '}';
    }
}
