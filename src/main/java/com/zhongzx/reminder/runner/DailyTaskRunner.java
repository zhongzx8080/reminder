package com.zhongzx.reminder.runner;

import com.zhongzx.reminder.strategy.DailyTaskStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DailyTaskRunner implements ApplicationRunner {

    @Autowired
    private List<DailyTaskStrategy> dailyTasks;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (DailyTaskStrategy dailyTask : dailyTasks) {
            if (dailyTask.runOnStart()) {
                dailyTask.executeDailyTask();
            }
        }
    }
}
