package com.dova.dev.httpclient;

import org.apache.commons.codec.Charsets;
import org.apache.http.ContentTooLongException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.ContentDecoder;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.entity.ContentBufferEntity;
import org.apache.http.nio.protocol.AbstractAsyncResponseConsumer;
import org.apache.http.nio.util.HeapByteBufferAllocator;
import org.apache.http.nio.util.SimpleInputBuffer;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Asserts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class AsyncResponseConsumer extends AbstractAsyncResponseConsumer<Response> {
    private final static Logger LOG = LoggerFactory.getLogger(AsyncRequestProducer.class);
    public           String            sequence;
    private volatile HttpResponse response;
    private volatile SimpleInputBuffer buf;

    public AsyncResponseConsumer(String sequence) {
        this.sequence = sequence;
    }

    @Override
    protected void onResponseReceived(HttpResponse response) throws IOException {
        this.response = response;
        LOG.info("Response received, sequence = {}, status = {}", sequence, response.getStatusLine().getStatusCode());
    }

    @Override
    protected void onContentReceived(ContentDecoder decoder, IOControl ioctrl) throws IOException {
        Asserts.notNull(this.buf, "Content buffer");
        this.buf.consumeContent(decoder);
    }

    @Override
    protected void onEntityEnclosed(HttpEntity entity, ContentType contentType) throws IOException {
        long len = entity.getContentLength();
        if (len > Integer.MAX_VALUE) {
            throw new ContentTooLongException("Entity content is too long: " + len);
        }
        if (len < 0) {
            len = 4096;
        }
        this.buf = new SimpleInputBuffer((int) len, new HeapByteBufferAllocator());
        this.response.setEntity(new ContentBufferEntity(entity, this.buf));
        //LOG.info("Response received from service[{}] on orderNo = {}", service, orderNo);
    }

    @Override
    protected Response buildResult(HttpContext context) throws Exception {
        int httpCode = response.getStatusLine().getStatusCode();
        String body  = "";
        if(httpCode != 204){
            body = EntityUtils.toString(response.getEntity(), Charsets.UTF_8);
        }
        if(httpCode /100 != 2){
            LOG.info("Get Async Result sequence:{} code:{} body:{}",sequence, httpCode, body);
        }else{
            LOG.info("Get Async Result sequence:{} code:{}",sequence, httpCode);
        }
        return Response.of(sequence, body, httpCode);
    }

    @Override
    protected void releaseResources() {
        if(response != null && response.getEntity() != null){
            EntityUtils.consumeQuietly(response.getEntity());
        }
    }
}
