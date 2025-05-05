package com.isluel.springbatch.batch.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "product")
public class Product {
    @Id
    private Long id;

    private String name;
    private int price;
    private String type;
}
