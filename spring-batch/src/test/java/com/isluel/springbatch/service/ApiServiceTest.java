package com.isluel.springbatch.service;

import com.isluel.springbatch.batch.domain.ApiInfo;
import com.isluel.springbatch.batch.domain.ApiRequestVO;
import com.isluel.springbatch.batch.domain.ProductVO;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Disabled
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ApiServiceTest {

    @Autowired
    ApiService1 apiService1;

    @DisplayName("생성된 RestTemlate로 데이터를 전송하여 정상 결과를 전달 받는다.")
    @Test
    public void doApiService() {
        // given
        ProductVO vo = new ProductVO();
        vo.setId(1L);
        vo.setType("1");
        var requestVO = ApiRequestVO.builder()
                .id(1L)
                .productVO(vo)
                .build();

        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        RestTemplate restTemplate = restTemplateBuilder.errorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {

            }
        }).build();

        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        var apiInfo = ApiInfo.builder().apiRequestList(List.of(requestVO))
                .build();

        // when
        var result = apiService1.doApiService(restTemplate, apiInfo);

        // then
        assertThat(result).isNotNull();
        assertThat(result).extracting("status", "message")
                .containsExactly(200, "product 1 was successfully processed");

    }

    @DisplayName("데이터를 전송하여 정상 결과를 전달 받는다.")
    @Test
    public void apiRequest() {
        // given
        ProductVO vo = new ProductVO();
        vo.setId(1L);
        vo.setType("1");
        var requestVO = ApiRequestVO.builder()
                .id(1L)
                .productVO(vo)
                .build();

        // when
        var result = apiService1.service(List.of(requestVO));

        // then
        assertThat(result).isNotNull();
        assertThat(result).extracting("status", "message")
                .containsExactly(200, "product 1 was successfully processed");
    }

}