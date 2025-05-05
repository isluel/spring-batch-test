package com.isluel.springbatch.batch.job.file;

import com.isluel.springbatch.TestBatchConfig;
import com.isluel.springbatch.batch.domain.ProductVO;
import org.junit.After;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBatchTest
@SpringBootTest(classes = {FileJobConfiguration.class, TestBatchConfig.class})
@ActiveProfiles("test")
public class FileJobConfigurationTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @DisplayName("requestDate 값을 전달 받아 파일에 저장된 값을 가져온다.")
    @Test
    public void fileStepTest() throws Exception {
        // given
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("requestDate", "20210101")
                .addLong("id", new Date().getTime())
                .toJobParameters();

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchStep("fileStep", jobParameters);

        // then
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

        StepExecution stepExecution = (StepExecution) ((List) jobExecution.getStepExecutions()).get(0);

        assertThat(stepExecution.getReadCount()).isEqualTo(30);
        assertThat(stepExecution.getWriteCount()).isEqualTo(30);
    }

    @Autowired
    private FlatFileItemReader<ProductVO> fileItemReader;

    @DisplayName("requestDate에 따라 csv를 정삭적으로 읽는다.")
    @Test
    public void fileItemReaderTest() throws Exception {
        // given
        String requestDate = "20210101";
        ExecutionContext ctx = new ExecutionContext();
        ctx.put("requestDate", requestDate);
        fileItemReader.open(ctx);

        // when
        ProductVO first = fileItemReader.read();
        ProductVO second = fileItemReader.read();
        ProductVO third = fileItemReader.read(); // null

        // then
        assertThat(first).isNotNull();
        assertThat(first.getId()).isEqualTo(1L);
        assertThat(second.getName()).isEqualTo("item2");
        assertThat(third).isNotNull();
    }

    @Bean
    public StepExecution getStepExecution() {
        // Create a StepExecution with necessary job parameters or context
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("requestDate", "20210101")
                .addLong("id", new Date().getTime())
                .toJobParameters();

        StepExecution stepExecution = new StepExecution("fileStep", new JobExecution(1L, jobParameters));
        return stepExecution;
    }
}