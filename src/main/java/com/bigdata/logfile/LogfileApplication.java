package com.bigdata.logfile;

import cn.hutool.cron.CronUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LogfileApplication {
    private static final Log log = LogFactory.get();

    public static void main(String[] args) {
        SpringApplication.run(LogfileApplication.class, args);

        try {
            CronUtil.setMatchSecond(true);
            CronUtil.start();
        } catch (Exception e){
            CronUtil.restart();
        }
        log.info("上传日志项目启动成功");
    }

}
