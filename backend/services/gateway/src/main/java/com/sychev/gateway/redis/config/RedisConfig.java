package com.sychev.gateway.redis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import redis.clients.jedis.Jedis;

import javax.validation.constraints.NotNull;

@Validated
@Configuration
@EnableConfigurationProperties(RedisConfig.class)
@ConfigurationProperties(prefix = "application.properties")
public class RedisConfig {

    @NotNull
    @Value("${redis.host}")
    private String host;
    @Value("${redis.port}")
    private int port;

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Bean
    public Jedis jedis() {
        return new Jedis(host, port);
    }
}