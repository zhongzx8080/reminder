package com.zhongzx.reminder.strategy;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;

@Slf4j
public abstract class DailyTaskStrategy implements EnvironmentAware, BeanNameAware {

    private Environment environment;

    private String beanName;

    @Async
    public void executeDailyTask() {
        if (!enable()) {
            log.info("{} 任务未启用", beanName);
            return;
        }
        if (checkExecute()) {
            boolean success = doDailyTask();
            while (!success) {
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {

                }
                success = doDailyTask();
            }
        }
        shutdown();
    }

    private boolean enable() {
        String property = environment.getProperty(beanName);
        boolean enabled = StrUtil.isNotBlank(property) && (StrUtil.equalsIgnoreCase(property, "true") || StrUtil.equals(property, "1"));
        return enabled;
    }

    private void shutdown() {
        String property = environment.getProperty("daily_task_all");
        boolean enableAll = StrUtil.isNotBlank(property) && (StrUtil.equalsIgnoreCase(property, "true") || StrUtil.equals(property, "1"));
        if (enableAll) {
            return;
        }
        log.info("{} 退出程序", beanName);
        System.exit(0);
    }

    public abstract boolean checkExecute();

    public abstract boolean doDailyTask();

    // 是否程序启动后执行默认是
    public boolean runOnStart() {
        return true;
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
