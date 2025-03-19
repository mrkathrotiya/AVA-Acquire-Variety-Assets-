package com.server.AVA.Implementations;

import com.server.AVA.Services.MailService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MailServiceImpl implements MailService {
    private static final Logger log = LoggerFactory.getLogger(MailServiceImpl.class);
    private final JavaMailSender javaMailSender;


    @Override
    public void sendOtpMail(String to, String subject,String otp) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(to);
            mail.setSubject(subject);
            String message = "Here your Onr-Time-Password for reset credentials: "+otp+"\n Valid for 10 minutes";
            mail.setText(message);
            javaMailSender.send(mail);
        }catch (Exception e){
            log.error("Exception while send mail",e);
        }
    }

    @Override
    public void sendConfirmationMail(String to) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(to);
            mailMessage.setSubject("Registration confirmed!");
            mailMessage.setText("You recently registered to AVA with mail Id: "+to);
            javaMailSender.send(mailMessage);
        }catch (Exception e){
            log.error("Exception while sending confirmation mail!");
        }

    }
}
