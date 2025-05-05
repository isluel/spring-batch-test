package com.isluel.springbatch.scheduler;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static org.quartz.JobBuilder.newJob;

@Component
@Profile("!test") // "test" profile일 때는 실행 안 함
public class ApiJobRunner extends JobRunner {

    @Autowired
    private Scheduler scheduler;

    @Override
    public void doRun(ApplicationArguments args) {

        JobDetail jobDetail = buildJobDetail(ApiSchJob.class, "apiJob", "batch", new HashMap<>());
        Trigger trigger = buildJobTrigger("0/30 * * * * ?");


        try {
            // Quartz의 Job의 정보를 기반으로 trigger에서 설정한 cron 시간에 맞춰서 Schedule을 수행한다.
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
