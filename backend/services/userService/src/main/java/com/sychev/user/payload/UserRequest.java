package com.sychev.user.payload;

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

    public UserRequest() {}

    public UserRequest(String token, UUID uuid) {
        this.token = token;
        this.userUuid = uuid;
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
