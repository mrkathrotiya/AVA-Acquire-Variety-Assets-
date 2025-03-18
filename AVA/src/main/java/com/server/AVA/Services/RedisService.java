package com.server.AVA.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.AVA.Models.Property;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class RedisService {
    private static final Logger log = LoggerFactory.getLogger(RedisService.class);
    private final RedisTemplate redisTemplate;

    public <T> T get(String key, Class<T> entityClass){
        try {
            Object o = redisTemplate.opsForValue().get(key);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(Objects.requireNonNull(o.toString()), entityClass);
        }catch (Exception e){
            log.error("Exception: {}", e);
            return  null;
        }
    }

    public void set(String key, Object o, Long etl){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonValue = objectMapper.writeValueAsString(o);
             redisTemplate.opsForValue().set(key,jsonValue,etl, TimeUnit.SECONDS);
        }catch (Exception e) {
                log.error("Exception: {}", e);
        }
    }

}
