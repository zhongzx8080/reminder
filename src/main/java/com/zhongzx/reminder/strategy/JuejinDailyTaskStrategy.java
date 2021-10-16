package com.zhongzx.reminder.strategy;

import com.zhongzx.reminder.juejin.JuejinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(DailyTaskStrategyConstant.JUE_JIN)
public class JuejinDailyTaskStrategy extends DailyTaskStrategy {

    @Autowired
    private JuejinService juejinService;

    @Override
    public boolean checkExecute() {
        return true;
    }

    @Override
    public boolean doDailyTask() {
        boolean checkin = juejinService.checkin();
        boolean draw = juejinService.draw();
        return checkin && draw;
    }
}
