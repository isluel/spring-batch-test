package com.isluel.springbatch.batch.chunk.processor;

import com.isluel.springbatch.TestBatchConfig;
import com.isluel.springbatch.batch.domain.Product;
import com.isluel.springbatch.batch.domain.ProductVO;
import com.isluel.springbatch.batch.job.file.FileJobConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBatchTest
@SpringBootTest(classes = {FileJobConfiguration.class, TestBatchConfig.class})
@ActiveProfiles("test")
public class FileItemProcessorTest {

    @Autowired
    ItemProcessor<ProductVO, Product> fileItemProcessor;

    @DisplayName("ProductVO를 Product 로 변환한다.")
    @Test
    public void processTest() throws Exception {
        // given
        ProductVO vo = ProductVO.builder()
                .id(1L)
                .name("item1")
                .price(1000)
                .type("1")
                .build();

        // when
        var result = (Product) fileItemProcessor.process(vo);

        // then
        assertThat(result).extracting("id", "name", "price", "type")
                .containsExactly(vo.getId(), vo.getName(), vo.getPrice(), vo.getType());
    }
}