package com.zhongzx.reminder.buyticket;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/*
 *
 * 节假日APi
 *
 * */
@Slf4j
public class HolidayUtil {

    public static final String API = "https://api.apihubs.cn/holiday/get";


    /*
     *
     * 判断是否为工作日
     * 首页 http://www.apihubs.cn/#/holiday
     *
     * https://api.apihubs.cn/holiday/get?field=workday&date=20211020&size=31
     * {"code":"0","msg":"ok","data":{"list":[{"workday":1}],"page":1,"size":31,"total":1}}
     *
     * */
    public static boolean isWorkDay(String date) {
        String url = String.format("%s?field=workday&date=%s", API, date);
        boolean workday = true;

        try {
            String response = HttpUtil.get(url);
            JSONObject responseJson = JSONUtil.parseObj(response);
            if (Objects.equals("0", responseJson.get("code"))) {
                JSONObject responseDataJson = responseJson.get("data", JSONObject.class);
                JSONArray listJson = responseDataJson.getJSONArray("list");
                workday = listJson.stream().map(item -> JSONUtil.parseObj(item)).allMatch(item -> item.getInt("workday") == 1);
            }
        } catch (Exception exception) {
            log.error("判断工作日异常 {}", exception.getMessage());
        }

        return workday;
    }
}
