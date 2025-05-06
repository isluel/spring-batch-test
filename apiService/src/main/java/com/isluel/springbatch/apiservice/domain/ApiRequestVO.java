package com.isluel.springbatch.apiservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiRequestVO {
    private long id;

    private ProductVO productVO;
}
