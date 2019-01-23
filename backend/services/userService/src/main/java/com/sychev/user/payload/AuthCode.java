package com.sychev.user.payload;

import java.util.UUID;

public class AuthCode {

    private UUID code;

    public UUID getCode() {
        return code;
    }

    public void setCode(UUID code) {
        this.code = code;
    }
}
