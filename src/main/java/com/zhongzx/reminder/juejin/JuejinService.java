package com.zhongzx.reminder.juejin;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class JuejinService {

    @Autowired
    private JuejinConfig juejinConfig;

    private static final String CHECKIN_URL = "https://api.juejin.cn/growth_api/v1/check_in";

    private static final String DRAW_URL = "https://api.juejin.cn/growth_api/v1/lottery/draw";

    private static final String DRAW_FREE_URL = "https://api.juejin.cn/growth_api/v1/get_today_status";

    private static final String COOKIE_HEADER = "cookie";

    private static final String ERR_NO = "err_no";

    // 签到
    public boolean checkin() {
        boolean success = false;

        try {
            String response = HttpRequest.post(CHECKIN_URL).addHeaders(getHeaders()).execute().body();
            log.info("{} \n {} ", CHECKIN_URL, response);
            JSONObject responseJson = JSONUtil.parseObj(response);
            Integer code = responseJson.getInt(ERR_NO);
            /*
             *
             *
             * 15001=重复签到
             *
             * */
            success = Objects.equals(code, 0) || Objects.equals(code, 15001);
            log.info("掘金签到结果 {}", success);
        } catch (Exception e) {
            log.error("掘金签到异常 {}", e.getMessage());
        }
        return success;
    }

    // 抽奖
    public boolean draw() {
        boolean success = drawFree();
        if (success) {
            log.info("掘金免费抽奖结果 {}", success);
            return success;
        }
        try {
            String response = HttpRequest.post(DRAW_URL).addHeaders(getHeaders()).execute().body();
            log.info("{} \n {} ", DRAW_URL, response);
            JSONObject responseJson = JSONUtil.parseObj(response);

            Integer code = responseJson.getInt(ERR_NO);
            success = Objects.equals(code, 0);
            log.info("掘金抽奖结果 {}", success);
        } catch (Exception e) {
            log.error("掘金抽奖失败 {}", e.getMessage());
        }
        return success;
    }

    // 是否免费抽奖
    public boolean drawFree() {
        boolean success = false;
        try {
            String response = HttpRequest.get(DRAW_FREE_URL).addHeaders(getHeaders()).execute().body();
            log.info("{} \n {} ", DRAW_FREE_URL, response);
            JSONObject responseJson = JSONUtil.parseObj(response);

            Integer code = responseJson.getInt(ERR_NO);
            success = Objects.equals(code, 0) && responseJson.getBool("data", false);
        } catch (Exception e) {
            log.error("掘金免费抽奖失败 {}", e.getMessage());
        }
        return success;
    }

    public Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put(COOKIE_HEADER, juejinConfig.getCookie());
        headers.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Safari/537.36");
        headers.put("cache-control", " no-cache");
        return headers;
    }
}
