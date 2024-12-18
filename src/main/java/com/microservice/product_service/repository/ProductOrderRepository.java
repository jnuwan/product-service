package com.microservice.product_service.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.microservice.product_service.model.ProductOrder;

@Repository
public interface ProductOrderRepository extends CrudRepository<ProductOrder, Long> {

}
