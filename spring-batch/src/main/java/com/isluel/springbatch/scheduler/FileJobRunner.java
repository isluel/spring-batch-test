package com.isluel.springbatch.scheduler;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Profile("!test") // "test" profile일 때는 실행 안 함
public class FileJobRunner extends JobRunner{

    @Autowired
    private Scheduler scheduler;

    @Override
    public void doRun(ApplicationArguments args) {
        JobDetail jobDetail = buildJobDetail(FileSchJob.class, "fileJob", "batch", new HashMap<>());
        Trigger trigger = buildJobTrigger("0/50 * * * * ?");

        String[] sourceArgs = args.getSourceArgs();
        jobDetail.getJobDataMap().put("requestDate", sourceArgs[0]);

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
