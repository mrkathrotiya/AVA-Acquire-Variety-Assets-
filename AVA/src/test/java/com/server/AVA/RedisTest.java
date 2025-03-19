package com.server.AVA;

import com.server.AVA.Models.Land;
import com.server.AVA.Services.MailService;
import com.server.AVA.Services.PropertyTypeServices.LandService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class RedisTest {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
            private LandService landService;

    //@Test
    void SendMailTest(){
        //redisTemplate.opsForValue().set("email","abc@gmail.com");
        Object o=redisTemplate.opsForValue().get("name");
        int a = 1;
    }

    @Test
    void GetLandTest() throws Exception {
        Land land = landService.getLandByPropertyId(9L);
        System.out.println(land.toString());
    }
}
