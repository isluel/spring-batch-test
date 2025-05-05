package com.isluel.springbatch.scheduler;

import lombok.SneakyThrows;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

@Component
public class FileSchJob extends QuartzJobBean {

    @Autowired
    Job fileJob;

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    JobExplorer jobExplorer;

    @SneakyThrows
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String requestDate = (String) jobExecutionContext.getJobDetail().getJobDataMap().get("requestDate");

        // DB의 JobExecution Params 을 확인하면 KeyName Value String 으로 requestDate 와 값이 저장되어있다.
        // 이미 저장되어 있는 데이터면 실행하지않도록 한다.
        // 1. FileJob 이름의 인스턴스를 가져옴
        var jobInstanceCount = jobExplorer.getJobInstanceCount(fileJob.getName());
        // 2. 모든 JobInstacne 가져옴
        var jobInstances = jobExplorer.getJobInstances(fileJob.getName(), 0, jobInstanceCount);
        // 3. loop로 해당되는 값을 가져옴.
        if (!jobInstances.isEmpty()) {
            for (var jobInstance : jobInstances) {
                var jobExecutions = jobExplorer.getJobExecutions(jobInstance);
                var jobExecutionList =  jobExecutions.stream().filter(j
                        -> j.getJobParameters().getString("requestDate").equalsIgnoreCase(requestDate))
                        .collect(Collectors.toList());

                // 4. 해당 날짜가 있으면 예외 발생시키서 실행하지 않도록 한다.
                if (!jobExecutionList.isEmpty()) {
                    throw new JobExecutionException(requestDate + " is already exists");
                }
            }
        }


        var jobParameters = new JobParametersBuilder()
                .addLong("id", new Date().getTime())
                .addString("requestDate", requestDate)
                        .toJobParameters();

        jobLauncher.run(fileJob, jobParameters);
    }
}
