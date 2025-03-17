package com.server.AVA.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.AVA.Models.Property;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class RedisService {
    private static final Logger log = LoggerFactory.getLogger(RedisService.class);
    private final RedisTemplate redisTemplate;

    public <T> T get(Long key, Class<T> entityClass){
        try {
            Object o = redisTemplate.opsForValue().get(String.valueOf(key));
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(o.toString(), entityClass);
        }catch (Exception e){
            log.error("Exception: {}", e);
            return  null;
        }
    }

    public void set(Long key, Object o, Long etl){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonValue = objectMapper.writeValueAsString(o);
             redisTemplate.opsForValue().set(String.valueOf(key),jsonValue,etl, TimeUnit.SECONDS);
        }catch (Exception e) {
                log.error("Exception: {}", e);
        }
    }

}
