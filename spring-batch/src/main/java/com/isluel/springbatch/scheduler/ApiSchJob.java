package com.isluel.springbatch.scheduler;

import lombok.SneakyThrows;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ApiSchJob extends QuartzJobBean {

    @Autowired
    private Job apiJob;

    @Autowired
    private JobLauncher jobLauncher;

    @SneakyThrows
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        var jobParameters = new JobParametersBuilder()
                // 실행시 마다 다른 Parameter를 넣을수 있도록 한다.
                .addLong("id", new Date().getTime())
                .toJobParameters();

        jobLauncher.run(apiJob, jobParameters);
    }
}
