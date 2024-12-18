package com.microservice.product_service.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.microservice.product_service.dto.ProductDto;

public interface ProductSearchService {

	List<ProductDto> searchProductByText(String query, Pageable pageable);

	List<ProductDto> searchPhysicalProductByText(String query, Pageable pageable);

	List<ProductDto> searchDigitalProductByText(String query, Pageable pageable);

}
