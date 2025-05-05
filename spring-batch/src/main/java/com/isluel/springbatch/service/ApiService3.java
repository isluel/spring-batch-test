package com.isluel.springbatch.service;

import com.isluel.springbatch.batch.domain.ApiInfo;
import com.isluel.springbatch.batch.domain.ApiResponseVO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiService3 extends AbstractApiService {

    @Override
    protected ApiResponseVO doApiService(RestTemplate restTemplate, ApiInfo apiInfo) {
        var responseEntity = restTemplate.
                postForEntity("http://localhost:8081/api/product/3", apiInfo, String.class);

        int status = responseEntity.getStatusCodeValue();
        var responseVO = ApiResponseVO.builder()
                .status(status)
                .message(responseEntity.getBody())
                .build();

        return responseVO;
    }
}
