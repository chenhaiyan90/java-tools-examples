package com.dova.dev.async_http;


import com.google.common.base.Throwables;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.nio.protocol.BasicAsyncRequestProducer;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsyncRequestProducer extends BasicAsyncRequestProducer {
    private final static Logger LOG = LoggerFactory.getLogger(AsyncRequestProducer.class);
    private String sequence;
    private String url;

    public AsyncRequestProducer(String sequence, HttpHost target, HttpRequest request) {
        super(target, request);
        this.sequence = sequence;
        url = request.getRequestLine().getMethod()+ "\t" + target.toString() + request.getRequestLine().getUri();
    }

    @Override
    public void requestCompleted(HttpContext context) {
        LOG.info("Request submitted, sequence = {} url:{}", sequence, url);
    }

    @Override
    public void failed(Exception ex) {
        LOG.warn("Request failed, sequence = {}, url:{},  error={}:{}", sequence, url, ex.getClass().getName(), Throwables.getRootCause(ex).getMessage());
    }
}
