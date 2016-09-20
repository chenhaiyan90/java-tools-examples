package com.dova.dev.httpclient;


import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpUriRequest;


public class Message {

    public final String                  sequence;
    private      AsyncRequestProducer  producer;
    private      AsyncResponseConsumer consumer;


    public Message(String sequence, HttpHost target, HttpUriRequest request) {
        this.sequence = sequence;
        this.producer = new AsyncRequestProducer(sequence, target, request);
        this.consumer = new AsyncResponseConsumer(sequence);
    }


    public AsyncRequestProducer getProducer() {
        return producer;
    }

    public AsyncResponseConsumer getConsumer() {
        return consumer;
    }
}

