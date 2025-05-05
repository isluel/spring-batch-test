package com.isluel.springbatch.apiservice.web;

import com.isluel.springbatch.apiservice.domain.ApiInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
public class ApiController {

    @RequestMapping("/{typeId}")
    public ResponseEntity<?> productTypeId(@RequestBody ApiInfo apiInfo, @PathVariable String typeId) {

        var itemList = apiInfo
                .getApiRequestList().stream().map(item -> item.getProductVO()).toList();

        System.out.println("productVOList = " + itemList);

        var result = "product %s was successfully processed".formatted(typeId);

        return ResponseEntity.ok(result);
    }
}
