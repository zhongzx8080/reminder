package com.zhongzx.reminder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ReminderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReminderApplication.class, args);
        
        
        // test buy ticket
        try {
            Thread.sleep(1000 * 60 * 3);
            System.exit(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
    }

}
