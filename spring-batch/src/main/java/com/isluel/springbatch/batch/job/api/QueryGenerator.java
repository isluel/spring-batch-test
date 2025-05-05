package com.isluel.springbatch.batch.job.api;

import com.isluel.springbatch.batch.domain.ProductVO;
import com.isluel.springbatch.batch.rowmapper.ProductRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class QueryGenerator {

    public static ProductVO[] getProductList(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        // type 별로 Thread를 만든다.
        List<ProductVO> productVOList = jdbcTemplate.query("select type from product group by type", new ProductRowMapper(){
            @Override
            public ProductVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                return ProductVO.builder().type(rs.getString("type")).build();
            }
        });

        return productVOList.toArray(new ProductVO[0]);
    }

    public static Map<String, Object> getParameterForQuery(String parameter, String value) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put(parameter, value);
        return parameters;
    }
}
