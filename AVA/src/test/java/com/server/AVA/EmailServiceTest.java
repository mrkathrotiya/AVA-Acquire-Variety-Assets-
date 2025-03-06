package com.server.AVA;

import com.server.AVA.Implimantations.MailServiceImpl;
import com.server.AVA.Services.MailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class EmailServiceTest {
    @Autowired
    private MailService mailService;

    @Test
    void SendMailTest(){
        mailService.sendMail("email address where you want to send",
                "Testing java mail sender",
                "Hi how are you");
    }
}
