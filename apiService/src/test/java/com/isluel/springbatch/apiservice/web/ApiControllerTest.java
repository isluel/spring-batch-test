package com.isluel.springbatch.apiservice.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isluel.springbatch.apiservice.domain.ApiInfo;
import com.isluel.springbatch.apiservice.domain.ApiRequestVO;
import com.isluel.springbatch.apiservice.domain.ProductVO;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ApiController.class)
@ExtendWith(MockitoExtension.class)
class ApiControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("Type에 맞는 Product를 조회한다.")
    @Test
    void productTypeId() throws Exception {
        // given
        String typeId = "1";
        String url = "/api/product/" + typeId;

        ProductVO productVO = ProductVO.builder()
                .id(1L)
                .type(typeId)
                .name("item1")
                .price(1000)
                .build();
        ApiRequestVO requestVO = ApiRequestVO.builder()
                .id(productVO.getId())
                .productVO(productVO)
                .build();
        ApiInfo info = ApiInfo.builder()
                .url(url)
                .apiRequestList(List.of(requestVO))
                .build();

        // when
        var result = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(info))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        // then
        assertThat(result.getResponse().getContentAsString())
                .isEqualTo("product 1 was successfully processed");
    }
}