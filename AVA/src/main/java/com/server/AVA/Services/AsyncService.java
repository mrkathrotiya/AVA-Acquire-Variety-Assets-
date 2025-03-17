package com.server.AVA.Services;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class AsyncService {
    private static final Logger log = LoggerFactory.getLogger(AsyncService.class);
    private final MailService mailService;
    @Async
    public void sendConfirmationMail(String to) throws InterruptedException{
        log.info("Process started with thread {}", Thread.currentThread().getName());
        mailService.sendConfirmationMail(Objects.requireNonNull(to));
        TimeUnit.SECONDS.sleep(3);
        log.info("Process finished with thread {}", Thread.currentThread().getName());
    }
}
