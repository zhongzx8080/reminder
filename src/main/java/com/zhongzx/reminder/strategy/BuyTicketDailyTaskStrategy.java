package com.zhongzx.reminder.strategy;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.zhongzx.reminder.buyticket.HolidayUtil;
import com.zhongzx.reminder.mail.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component(DailyTaskStrategyConstant.BUY_TICKET)
public class BuyTicketDailyTaskStrategy extends DailyTaskStrategy {

    private Map<String, Boolean> sendResultMap = new HashMap<>(8);

    @Autowired
    private MailService mailService;


    @Override
    public boolean checkExecute() {
        /*
         *
         * 每小时执行
         *
         *  明天是否是工作日
         *  否：结束
         *  是：提醒买票
         *
         * */
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        String date = LocalDateTimeUtil.format(tomorrow, "yyyyMMdd");
        boolean workDay = HolidayUtil.isWorkDay(date);
        if (!workDay) {
            log.info("【{}】非工作日", tomorrow);
            sendResultMap.clear();
            return false;
        }

        return true;
    }

    @Override
    public boolean doDailyTask() {
        boolean sent = mailService.send();

        log.info("【{}】发送提醒结果 {}", LocalDateTime.now(), sent);

        return sent;
    }
}
