package com.server.AVA.Services;

public interface MailService {
    void sendOtpMail(String to, String subject,String otp);
}
