package com.sychev.gateway.model;

import java.util.UUID;

public class UserRequest {

    public String token;

    public UUID userUuid;

    public UUID serviceUuid;

    public UUID getServiceUuid() {
        return serviceUuid;
    }

    public void setServiceUuid(UUID serviceUuid) {
        this.serviceUuid = serviceUuid;
    }

    public UserRequest (String token, UUID userUuid, UUID serviceUuid) {
        this.token = token;
        this.userUuid = userUuid;
        this.serviceUuid = serviceUuid;
    }

    public UUID getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(UUID userUuid) {
        this.userUuid = userUuid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
