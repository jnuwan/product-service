package com.microservice.product_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.microservice.product_service.dto.ProductDto;
import com.microservice.product_service.repository.DigitalProductRepository;
import com.microservice.product_service.repository.PhysicalProductRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductSearchServiceImpl implements ProductSearchService {

	@Autowired
	private PhysicalProductRepository physicalProductRepository;

	@Autowired
	private DigitalProductRepository digitalProductRepository;

	@Override
	public List<ProductDto> searchProductByText(String query, Pageable pageable) {
		log.info("Fetching products by free text {}.", query);
		return physicalProductRepository.searchPhysicalAndDigitalProducts(query, pageable);
	}

	@Override
	public List<ProductDto> searchPhysicalProductByText(String query, Pageable pageable) {
		log.info("Fetching physical products by free text {}.", query);
		return physicalProductRepository.searchPhysicalProducts(query, pageable);
	}

	@Override
	public List<ProductDto> searchDigitalProductByText(String query, Pageable pageable) {
		log.info("Fetching digital products by free text {}.", query);
		return digitalProductRepository.searchDigitalProducts(query, pageable);
	}

}
