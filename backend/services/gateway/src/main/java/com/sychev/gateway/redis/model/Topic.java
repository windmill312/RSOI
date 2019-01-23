package com.sychev.gateway.redis.model;

public enum Topic {

    TASK("queue#task"),
    TASK_30("queue#task30"),
    TASK_60("queue#task60");

    private String name;

    Topic(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
