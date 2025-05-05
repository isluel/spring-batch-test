package com.isluel.springbatch.apiservice.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiRequestVO {
    private long id;

    @Builder.Default
    private ProductVO productVO;

    public ProductVO getProductVO() {
        return this.productVO;
    }
}
