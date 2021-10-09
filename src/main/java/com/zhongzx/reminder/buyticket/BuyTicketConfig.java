package com.zhongzx.reminder.buyticket;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties("buyticket")
@Configuration
public class BuyTicketConfig {

    private String webhookUrl;

    private String content = "买票了";
}
