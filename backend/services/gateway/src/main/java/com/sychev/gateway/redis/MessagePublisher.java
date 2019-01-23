package com.sychev.gateway.redis;

import com.sychev.gateway.redis.model.Task;

public interface MessagePublisher {

    void publish(String topic, Task task);
}
