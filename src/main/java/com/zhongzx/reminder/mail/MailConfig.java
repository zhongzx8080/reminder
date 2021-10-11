package com.zhongzx.reminder.mail;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@ConfigurationProperties("mail")
@Configuration
public class MailConfig {
    
    private boolean enable;

    private String username;

    private String password;

    private List<String> to;

    private String subject;

    private String text;
}
