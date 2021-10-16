package com.zhongzx.reminder.juejin;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties("juejin")
@Configuration
public class JuejinConfig {

    private String cookie = "";
}
