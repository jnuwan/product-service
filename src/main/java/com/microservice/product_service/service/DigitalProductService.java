package com.microservice.product_service.service;

import java.util.List;

import com.microservice.product_service.dto.DigitalProductDto;
import com.microservice.product_service.model.DigitalProduct;

public interface DigitalProductService {

	List<DigitalProduct> searchAll();

	DigitalProduct searchById(Long id);

	DigitalProduct saveOrUpdate(DigitalProductDto digitalProductDto);

	void delete(Long productId);

}
