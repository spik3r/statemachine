package com.kaitait.statemachine;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedisService {

    private static final Logger LOG = LoggerFactory.getLogger(RedisService.class);

    public void doRedisFoo() {
        RedisClient redisClient = RedisClient
                .create("redis://localhost:6379/");
        StatefulRedisConnection<String, String> connection
                = redisClient.connect();

       connection.sync();
       RedisCommands<String, String> syncCommands = connection.sync();
       LOG.info("From redis: " + syncCommands.get("foo"));
       LOG.info("All keys: " +syncCommands.keys("*"));

       syncCommands.set("someId", "123");
        LOG.info("All keys: " +syncCommands.keys("*"));
    }
}
