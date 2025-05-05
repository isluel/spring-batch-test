package com.isluel.springbatch.batch.chunk.processor;

import com.isluel.springbatch.TestBatchConfig;
import com.isluel.springbatch.batch.domain.ApiRequestVO;
import com.isluel.springbatch.batch.domain.ProductVO;
import com.isluel.springbatch.batch.job.file.FileJobConfiguration;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBatchTest
@SpringBootTest(classes = {FileJobConfiguration.class, TestBatchConfig.class})
@ActiveProfiles("test")
public class ApiItemProcessorTest {

    @DisplayName("ProductVO 를 ApiRequestVO 로 변환한다.")
    @Test
    public void processTest() throws Exception {
        // given
        ProductVO vo1 = ProductVO.builder()
                .id(1L)
                .name("item1")
                .price(1000)
                .type("1")
                .build();
        ProductVO vo2 = ProductVO.builder()
                .id(2L)
                .name("item2")
                .price(2000)
                .type("2")
                .build();
        ProductVO vo3 = ProductVO.builder()
                .id(3L)
                .name("item3")
                .price(3000)
                .type("3")
                .build();

        var processor1 = new ApiItemProcessor1();
        var processor2 = new ApiItemProcessor2();
        var processor3 = new ApiItemProcessor3();

        // when
        var result1 = (ApiRequestVO) processor1.process(vo1);
        var result2 = (ApiRequestVO) processor2.process(vo2);
        var result3 = (ApiRequestVO) processor3.process(vo3);


        // then
        assertThat(result1.getId()).isEqualTo(vo1.getId());
        assertThat(result1.getProductVO())
                .extracting("id", "name", "price", "type")
                .containsExactly(vo1.getId(), vo1.getName(), vo1.getPrice(), vo1.getType());

        assertThat(result2.getId()).isEqualTo(vo2.getId());
        assertThat(result2.getProductVO())
                .extracting("id", "name", "price", "type")
                .containsExactly(vo2.getId(), vo2.getName(), vo2.getPrice(), vo2.getType());

        assertThat(result3.getId()).isEqualTo(vo3.getId());
        assertThat(result3.getProductVO())
                .extracting("id", "name", "price", "type")
                .containsExactly(vo3.getId(), vo3.getName(), vo3.getPrice(), vo3.getType());

    }

}