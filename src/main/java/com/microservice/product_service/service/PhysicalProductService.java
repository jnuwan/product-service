package com.microservice.product_service.service;

import java.util.List;

import com.microservice.product_service.dto.PhysicalProductDto;
import com.microservice.product_service.model.PhysicalProduct;

public interface PhysicalProductService {

	List<PhysicalProduct> searchAll();

	PhysicalProduct searchById(Long id);

	PhysicalProduct saveOrUpdate(PhysicalProductDto physicalProductDto);

	void delete(Long productId);
}
