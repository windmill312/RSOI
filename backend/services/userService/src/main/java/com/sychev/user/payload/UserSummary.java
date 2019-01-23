package com.sychev.user.payload;

import java.util.UUID;

public class UserSummary {
    private UUID uuid;
    private String username;
    private String name;

    public UserSummary(UUID uuid, String username, String name) {
        this.uuid = uuid;
        this.username = username;
        this.name = name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
