package com.isluel.springbatch.apiservice.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


@Data
@Builder
public class ApiInfo {
    private String url;

    @Builder.Default
    private List<ApiRequestVO> apiRequestList = new ArrayList<>();
}
