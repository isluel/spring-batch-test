package com.isluel.springbatch.batch.partition;

import com.isluel.springbatch.batch.domain.ProductVO;
import com.isluel.springbatch.batch.job.api.QueryGenerator;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class ProductPartitioner implements Partitioner {

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {

        ProductVO[] productList = QueryGenerator.getProductList(dataSource);
        Map<String, ExecutionContext> result = new HashMap<>();

        int number = 0;

        // 저장된 type의 종류에 맞게 Execution Context 생성
        for (var i = 0; i < productList.length; i++) {
            ExecutionContext executionContext = new ExecutionContext();

            result.put("partition" + number, executionContext);
            executionContext.put("product", productList[i]);

            number++;
        }

        return result;
    }
}
