package com.zhongzx.reminder.buyticket;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.zhongzx.reminder.mail.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class BuyTicketJob {

    private Map<String, Boolean> sendResultMap = new HashMap<>(8);

    @Autowired
    private MailService mailService;

    /*
     *
     * 每小时执行
     *
     *  明天是否是工作日
     *  否：结束
     *  是：提醒买票
     *
     * */
    //@Scheduled(cron = "0 0 20,21,22 * * ? ")
    //@Scheduled(cron = "0/10 * * * * ?")
    public void checkBuyTicket() {
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        String date = LocalDateTimeUtil.format(tomorrow, "yyyyMMdd");
        boolean workDay = HolidayUtil.isWorkDay(date);
        if (!workDay) {
            log.info("【{}】非工作日", tomorrow);
            sendResultMap.clear();
            return;
        }

        remind(date);
    }

    private void remind(String date) {

        if (Objects.equals(sendResultMap.get(date), Boolean.TRUE)) {
            // 已发送不重复发送
            log.info("【{}】已发送提醒", date);
            return;
        }
        boolean sended = mailService.send();

        log.info("【{}】发送提醒结果 {}", date, sended);
        sendResultMap.put(date, sended);
    }
}
