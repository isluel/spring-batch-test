package com.isluel.springbatch.batch.job.api;

import com.isluel.springbatch.TestBatchConfig;
import com.isluel.springbatch.batch.domain.ApiRequestVO;
import com.isluel.springbatch.batch.domain.ApiResponseVO;
import com.isluel.springbatch.batch.domain.ProductVO;
import com.isluel.springbatch.batch.job.file.FileJobConfiguration;
import com.isluel.springbatch.batch.partition.ProductPartitioner;
import com.isluel.springbatch.service.ApiService1;
import com.isluel.springbatch.service.ApiService2;
import com.isluel.springbatch.service.ApiService3;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.support.ClassifierCompositeItemProcessor;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBatchTest
@SpringBootTest(classes = {ApiJobChildConfiguration.class, ApiStepConfiguration.class, TestBatchConfig.class})
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ApiStepConfigurationTest {

    @MockBean
    private ApiService1 apiService1;
    @MockBean
    private ApiService2 apiService2;
    @MockBean
    private ApiService3 apiService3;

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private DataSource dataSource;

    @DisplayName("파티녀서가 Slave 작업을 정상적으로 분할한다.")
    @Test
    public void partitionerTest() {
        // given
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("insert into product(id, name, price, type) values(1, 'item1', 1000, '1')");
        jdbcTemplate.execute("insert into product(id, name, price, type) values(2, 'item2', 2000, '2')");
        jdbcTemplate.execute("insert into product(id, name, price, type) values(3, 'item3', 3000, '3')");

        ProductPartitioner partitioner = new ProductPartitioner();
        partitioner.setDataSource(dataSource);

        // when
        Map<String, ExecutionContext> partitions = partitioner.partition(3);

        // then
        assertThat(partitions).hasSize(3);
        assertThat((ProductVO)partitions.get("partition0").get("product"))
                .extracting("type").isEqualTo("1");
    }

    @Bean
    public StepExecution getStepExecution() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("requestDate", "20210101")
                .addLong("id", new Date().getTime())
                .toJobParameters();

        StepExecution stepExecution = new StepExecution("fileStep", new JobExecution(1L, jobParameters));

        ProductVO product = new ProductVO();
        product.setType("1");
        stepExecution.getExecutionContext().put("product", product);

        return stepExecution;
    }

    @Autowired
    private JdbcPagingItemReader<ProductVO> itemReader;

    @DisplayName("itemReader가 DB에서 데이터를 읽어온다.")
    @Test
    public void itemReaderTest() throws Exception {
        // given
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("insert into product(id, name, price, type) values(1, 'item1', 1000, '1')");
        jdbcTemplate.execute("insert into product(id, name, price, type) values(2, 'item2', 2000, '2')");
        jdbcTemplate.execute("insert into product(id, name, price, type) values(3, 'item3', 3000, '3')");
        String requestDate = "20210101";
        ExecutionContext ctx = new ExecutionContext();
        ctx.put("requestDate", requestDate);

        itemReader.open(ctx);

        // when
        ProductVO item = itemReader.read();

        // then
        assertThat(item).isNotNull();
        assertThat(item).extracting("type").isEqualTo("1");
        itemReader.close();
    }

    @Autowired
    StepBuilderFactory stepBuilderFactory;

    private final ItemProcessor<ProductVO, ApiRequestVO> processor = new ApiStepConfiguration(
            stepBuilderFactory, dataSource, apiService1, apiService2, apiService3).itemProcessor();

    @DisplayName("type 1 인 ProductVO인 경우 ApiItemProcessor1을 사용한다.")
    @Test
    public void apiItemProcessorTest() throws Exception {
        // given
        ProductVO vo = new ProductVO();
        vo.setId(1L);
        vo.setType("1");

        // when
        ApiRequestVO result = processor.process(vo);

        // then
        assertThat(result).isNotNull();
        assertThat(result).extracting("id").isEqualTo(1L);
        assertThat(result.getProductVO()).extracting("type").isEqualTo("1");
    }

    @Autowired
    private ItemWriter<ApiRequestVO> itemWriter;

    @DisplayName("type 이 1인 경우 ApiIteMWriter1를 사용한다.")
    @Test
    public void apiItemWriterTest() throws Exception {
        // given
        ProductVO vo = new ProductVO();
        vo.setId(1L);
        vo.setType("1");
        var requestVO = ApiRequestVO.builder()
                .id(1L)
                .productVO(vo)
                .build();

        // when
        BDDMockito.given(apiService1.service(any())).willReturn(
                ApiResponseVO.builder().status(200).message("apiService1 success").build()
        );
        BDDMockito.given(apiService2.service(any())).willReturn(
                ApiResponseVO.builder().status(200).message("apiService2 success").build()
        );
        BDDMockito.given(apiService3.service(any())).willReturn(
                ApiResponseVO.builder().status(200).message("apiService3 success").build()
        );
        itemWriter.write(List.of(requestVO));

        // then
        assertThat(requestVO.getId()).isEqualTo(1L);
        assertThat(requestVO.getProductVO()).extracting("type").isEqualTo("1");
        assertThat(requestVO.getApiResponseVO()).extracting("status", "message")
                .containsExactly(200, "apiService1 success");
    }

    @DisplayName("apiMasterSte p이 파티셔너로 병렬처리를 정상 수행한다. ")
    @Test
    public void apiMasterStepTest() {
        // given
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("id", new Date().getTime())
                .toJobParameters();

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("insert into product(id, name, price, type) values(1, 'item1', 1000, '1')");
        jdbcTemplate.execute("insert into product(id, name, price, type) values(2, 'item2', 2000, '2')");
        jdbcTemplate.execute("insert into product(id, name, price, type) values(3, 'item3', 3000, '3')");

        // when
        BDDMockito.given(apiService1.service(any())).willReturn(
                ApiResponseVO.builder().status(200).message("apiService1 success").build()
        );
        BDDMockito.given(apiService2.service(any())).willReturn(
                ApiResponseVO.builder().status(200).message("apiService2 success").build()
        );
        BDDMockito.given(apiService3.service(any())).willReturn(
                ApiResponseVO.builder().status(200).message("apiService3 success").build()
        );
        JobExecution jobExecution = jobLauncherTestUtils.launchStep("apiMasterStep", jobParameters);

        // then
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

        StepExecution stepExecution = (StepExecution) ((List) jobExecution.getStepExecutions()).get(0);

        assertThat(stepExecution.getReadCount()).isEqualTo(3);
        assertThat(stepExecution.getWriteCount()).isEqualTo(3);
    }
}