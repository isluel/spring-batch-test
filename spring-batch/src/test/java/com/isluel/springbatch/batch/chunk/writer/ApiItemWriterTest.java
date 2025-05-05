package com.isluel.springbatch.batch.chunk.writer;

import com.isluel.springbatch.TestBatchConfig;
import com.isluel.springbatch.batch.chunk.processor.ApiItemProcessor1;
import com.isluel.springbatch.batch.chunk.processor.ApiItemProcessor2;
import com.isluel.springbatch.batch.chunk.processor.ApiItemProcessor3;
import com.isluel.springbatch.batch.domain.ApiRequestVO;
import com.isluel.springbatch.batch.domain.ApiResponseVO;
import com.isluel.springbatch.batch.domain.ProductVO;
import com.isluel.springbatch.batch.job.file.FileJobConfiguration;
import com.isluel.springbatch.service.AbstractApiService;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBatchTest
@SpringBootTest(classes = {FileJobConfiguration.class, TestBatchConfig.class})
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ApiItemWriterTest {

    @MockBean
    private AbstractApiService apiService;

    @DisplayName("ApiRequestVO 으로 API 통신한다.")
    @Test
    public void apiItemWriterTest() throws Exception {
        // given
        ProductVO vo1 = ProductVO.builder()
                .id(1L)
                .name("item1")
                .price(1000)
                .type("1")
                .build();
        ApiRequestVO requestVO1 = ApiRequestVO.builder()
                .id(vo1.getId())
                .productVO(vo1)
                .build();

        ProductVO vo2 = ProductVO.builder()
                .id(2L)
                .name("item2")
                .price(2000)
                .type("2")
                .build();
        ApiRequestVO requestVO2 = ApiRequestVO.builder()
                .id(vo2.getId())
                .productVO(vo2)
                .build();

        ProductVO vo3 = ProductVO.builder()
                .id(3L)
                .name("item3")
                .price(3000)
                .type("3")
                .build();
        ApiRequestVO requestVO3 = ApiRequestVO.builder()
                .id(vo3.getId())
                .productVO(vo3)
                .build();

        var writer1 = new ApiItemWriter1(apiService);
        var writer2 = new ApiItemWriter2(apiService);
        var writer3 = new ApiItemWriter3(apiService);

        List<ApiRequestVO> arr1 = new ArrayList<>();
        arr1.add(requestVO1);
        List<ApiRequestVO> arr2 = new ArrayList<>();
        arr2.add(requestVO2);
        List<ApiRequestVO> arr3 = new ArrayList<>();
        arr3.add(requestVO3);

        // given
        BDDMockito.given(apiService.service(any())).willReturn(
                ApiResponseVO.builder().status(200).message("success").build()
        );
        writer1.write(arr1);
        writer2.write(arr2);
        writer3.write(arr3);

        // then
        assertThat(requestVO1.getProductVO())
                .extracting("id", "name", "price", "type")
                .containsExactly(vo1.getId(), vo1.getName(), vo1.getPrice(), vo1.getType());
        assertThat(requestVO1.getApiResponseVO())
                .extracting("status", "message")
                .containsExactly(200, "success");

        assertThat(requestVO2.getProductVO())
                .extracting("id", "name", "price", "type")
                .containsExactly(vo2.getId(), vo2.getName(), vo2.getPrice(), vo2.getType());
        assertThat(requestVO2.getApiResponseVO())
                .extracting("status", "message")
                .containsExactly(200, "success");

        assertThat(requestVO3.getProductVO())
                .extracting("id", "name", "price", "type")
                .containsExactly(vo3.getId(), vo3.getName(), vo3.getPrice(), vo3.getType());
        assertThat(requestVO3.getApiResponseVO())
                .extracting("status", "message")
                .containsExactly(200, "success");
    }
}