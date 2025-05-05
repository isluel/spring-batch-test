package com.isluel.springbatch.batch.chunk.processor;

import com.isluel.springbatch.batch.domain.Product;
import com.isluel.springbatch.batch.domain.ProductVO;
import org.modelmapper.ModelMapper;
import org.springframework.batch.item.ItemProcessor;

public class FileItemProcessor implements ItemProcessor<ProductVO, Product> {
    @Override
    public Product process(ProductVO item) throws Exception {
        ModelMapper modelMapper = new ModelMapper();
        var product = modelMapper.map(item, Product.class);

        return product;
    }
}
