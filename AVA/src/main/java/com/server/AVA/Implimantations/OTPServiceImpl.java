package com.server.AVA.Implimantations;

import com.server.AVA.Services.OTPService;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class OTPServiceImpl implements OTPService {
    private final StringRedisTemplate redisTemplate;
    @Override
    public String generateOTP(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));

        // Store OTP in Redis with a 10-minute expiry
        redisTemplate.opsForValue().set(email, otp, 10, TimeUnit.MINUTES);

        return otp;
    }

    @Override
    public boolean validateOTP(String email, String OTP) {
        String storedOtp = redisTemplate.opsForValue().get(email);
        return storedOtp.equals(OTP);
    }

    @Override
    public void deleteOTP(String email) {
        redisTemplate.delete(email);
    }


}
