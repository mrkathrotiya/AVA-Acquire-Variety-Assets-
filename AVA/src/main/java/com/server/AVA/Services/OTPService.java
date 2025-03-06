package com.server.AVA.Services;

public interface OTPService {
    String generateOTP(String email);
    boolean validateOTP(String email, String OTP);
    void deleteOTP(String email);
}
