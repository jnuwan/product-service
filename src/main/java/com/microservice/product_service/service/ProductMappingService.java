package com.microservice.product_service.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.microservice.product_service.dto.ProductMappingRequestDto;
import com.microservice.product_service.dto.SuggestionDto;

public interface ProductMappingService {

	String handleProductMapping(ProductMappingRequestDto mappingRequestDto);

	List<SuggestionDto> getMappedProducts(Pageable pageable);

	List<SuggestionDto> getUnMappedProducts(Pageable pageable);
}
