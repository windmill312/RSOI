package com.sychev.gateway.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import com.sychev.gateway.redis.model.Task;

@Service
public class RedisMessagePublisher implements MessagePublisher {

    private static final Logger logger = LoggerFactory.getLogger(RedisMessagePublisher.class);

    private final Jedis jedis;
    private final ObjectWriter objectWriter;

    @Autowired
    public RedisMessagePublisher(Jedis jedis) {
        this.jedis = jedis;
        objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    public void publish(String topic, Task task) {
        try {
            jedis.lpush(topic, objectWriter.writeValueAsString(task));
        } catch (JsonProcessingException e) {
            logger.warn(e.getMessage());
        }
    }
}
