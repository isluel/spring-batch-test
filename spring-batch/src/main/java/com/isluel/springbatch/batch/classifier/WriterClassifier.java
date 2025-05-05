package com.isluel.springbatch.batch.classifier;

import com.isluel.springbatch.batch.domain.ApiRequestVO;
import org.springframework.batch.item.ItemWriter;
import org.springframework.classify.Classifier;

import java.util.HashMap;
import java.util.Map;

public class WriterClassifier<C, T> implements Classifier<C, T> {

    Map<String, ItemWriter<ApiRequestVO>> writerMap = new HashMap<>();

    public void setWriterMap(Map<String, ItemWriter<ApiRequestVO>> writerMap) {
        this.writerMap = writerMap;
    }

    @Override
    public T classify(C classifiable) {
        return (T) writerMap.get(((ApiRequestVO)classifiable).getProductVO().getType());
    }
}
