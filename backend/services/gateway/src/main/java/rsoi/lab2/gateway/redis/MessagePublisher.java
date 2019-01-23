package rsoi.lab2.gateway.redis;

import rsoi.lab2.gateway.redis.model.Task;

public interface MessagePublisher {

    void publish(String topic, Task task);
}
