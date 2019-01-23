package com.sychev.gateway.redis.model;

import com.sychev.gateway.model.FlightInfo;

import java.util.UUID;

public class Task {

    private UUID id = UUID.randomUUID();
    private String url;
    private FlightInfo requestData;

    public Task () {}

    public Task(String url, FlightInfo requestData) {
        this.url = url;
        this.requestData = requestData;
    }

    public UUID getId() {
        return id;
    }

    public Task setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Task setUrl(String url) {
        this.url = url;
        return this;
    }

    public Object getRequestData() {
        return requestData;
    }

    public void setRequestData(FlightInfo requestData) {
        this.requestData = requestData;
    }
}
