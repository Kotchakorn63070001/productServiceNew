package com.example.productservice.query.rest;

import com.example.productservice.query.FindProductsQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductQueryController {

    @Autowired
    QueryGateway queryGateway;

    @GetMapping
    public List<ProductRestModel> getProducts(){
        FindProductsQuery query = new FindProductsQuery();
        // ส่ง query เข้าไปใน command bus
        return queryGateway.query(
                query,
                ResponseTypes.multipleInstancesOf(ProductRestModel.class)
        ).join();
    }
}
