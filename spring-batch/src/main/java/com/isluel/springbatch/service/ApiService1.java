package com.isluel.springbatch.service;

import com.isluel.springbatch.batch.domain.ApiInfo;
import com.isluel.springbatch.batch.domain.ApiResponseVO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiService1 extends AbstractApiService {

    @Override
    protected ApiResponseVO doApiService(RestTemplate restTemplate, ApiInfo apiInfo) {

        ResponseEntity<String> response = restTemplate
                .postForEntity("http://localhost:8081/api/product/1", apiInfo, String.class);

        int statusCodeValue = response.getStatusCodeValue();
        var apiResponse = ApiResponseVO.builder()
                .status(statusCodeValue)
                .message(response.getBody())
                .build();

        return apiResponse;
    }
}
