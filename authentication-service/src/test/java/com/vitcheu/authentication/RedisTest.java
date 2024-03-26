package com.vitcheu.authentication;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import jakarta.annotation.Resource;

@SpringBootTest
public class RedisTest {
    @Resource
    RedisTemplate redisTemplate;

    @Test
    void redisLoad(){
        System.out.println(redisTemplate);
    }

    @Test
    void optForValue(){
        ValueOperations ops = redisTemplate.opsForValue();
        ops.set("city", "北京");
        ops.set("city2", "广州",2,TimeUnit.MINUTES);

        String city =(String) ops.get("city");
        System.out.println("city:"+city);

        ops.setIfAbsent("lock", "1");
        ops.setIfAbsent("lock", "2");
    }
}
