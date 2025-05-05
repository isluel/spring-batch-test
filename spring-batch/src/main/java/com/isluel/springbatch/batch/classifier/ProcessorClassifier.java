package com.isluel.springbatch.batch.classifier;

import com.isluel.springbatch.batch.domain.ApiRequestVO;
import com.isluel.springbatch.batch.domain.ProductVO;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.classify.Classifier;

import java.util.HashMap;
import java.util.Map;

public class ProcessorClassifier<C, T> implements Classifier<C, T> {

    private Map<String, ItemProcessor<ProductVO, ApiRequestVO>> processorMap = new HashMap<>();

    public void setProcessorMap(Map<String, ItemProcessor<ProductVO, ApiRequestVO>> processorMap) {
        this.processorMap = processorMap;
    }

    @Override
    public T classify(C classifiable) {
        // C: ProductVO 객체.
        // Type에 따른 ItemProcessor를 반환하도록 함.
        return (T) processorMap.get(((ProductVO)classifiable).getType());
    }
}
