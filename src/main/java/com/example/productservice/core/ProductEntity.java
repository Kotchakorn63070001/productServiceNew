package com.example.productservice.core;

// entity ที่ map กับตาราง Product ใน DB

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Data
public class ProductEntity implements Serializable {

    @Id
    @Column(unique = true) //บอกว่าห้ามซ้ำกัน
    private String productId;
    private String title;
    private BigDecimal price;
    private Integer quantity;

}
