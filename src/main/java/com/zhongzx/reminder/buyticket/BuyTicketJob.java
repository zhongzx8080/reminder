package com.zhongzx.reminder.buyticket;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
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

    @Autowired
    private BuyTicketConfig buyTicketConfig;

    private Map<String, Boolean> sendResultMap = new HashMap<>(8);

    /*
     *
     * 每小时执行
     *
     *  明天是否是工作日
     *  否：结束
     *  是：提醒买票
     *
     * */
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void checkBuyTicket() {
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        String date = LocalDateTimeUtil.format(tomorrow, "yyyyMMdd");
        boolean workDay = HolidayUtil.isWorkDay(date);
        if (!workDay) {
//            log.info("【{}】非工作日", tomorrow);
            sendResultMap.clear();
            return;
        }

        remind(date);
    }

    /*
     *
     * 通过向企业微信群机器人发送消息提醒
     * https://work.weixin.qq.com/api/doc/90000/90136/91770
     *
     * */
    private void remind(String date) {

        if (Objects.equals(sendResultMap.get(date), Boolean.TRUE)) {
            // 已发送不重复发送
//            log.info("【{}】已发送提醒", date);
            return;
        }

        String content = String.format("{\n" +
                "    \"msgtype\": \"text\",\n" +
                "    \"text\": {\n" +
                "        \"content\": \"%s\",\n" +
                "        \"mentioned_list\":[\"@all\"]\n" +
                "    }\n" +
                "}\n", buyTicketConfig.getContent());
        boolean sended;
        try {
            String response = HttpUtil.post(buyTicketConfig.getWebhookUrl(), content);
            JSONObject responseJson = JSONUtil.parseObj(response);
            sended = responseJson.getInt("errcode") == 0;
        } catch (Exception exception) {
            sended = false;
        }

//        log.info("【{}】发送提醒结果 {}", date, sended);
        sendResultMap.put(date, sended);
    }
}
