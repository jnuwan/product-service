package com.microservice.product_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.microservice.product_service.dto.ProductMappingRequestDto;
import com.microservice.product_service.dto.SuggestionDto;
import com.microservice.product_service.exception.NoProductExistsException;
import com.microservice.product_service.model.DigitalProduct;
import com.microservice.product_service.model.PhysicalProduct;
import com.microservice.product_service.repository.DigitalProductRepository;
import com.microservice.product_service.repository.PhysicalProductRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductMappingServiceImpl implements ProductMappingService {

	@Autowired
	private PhysicalProductRepository physicalProductRepository;

	@Autowired
	private DigitalProductRepository digitalProductRepository;

	@Override
	public String handleProductMapping(ProductMappingRequestDto mappingRequestDto) {
		log.info("Creating physical ({}) and digital ({}) product mapping.", mappingRequestDto.getPhysicalProductId(),
				mappingRequestDto.getDigitalProductId());
		PhysicalProduct physicalProduct = physicalProductRepository.findById(mappingRequestDto.getPhysicalProductId())
				.orElseThrow(() -> new NoProductExistsException("No physical product found."));
		log.info("Found physical product.");

		DigitalProduct digitalProduct = digitalProductRepository.findById(mappingRequestDto.getDigitalProductId())
				.orElseThrow(() -> new NoProductExistsException("No digital product found."));
		log.info("Found digital product.");

		digitalProduct.setPhysicalProduct(physicalProduct);
		digitalProductRepository.save(digitalProduct);

		log.info("Created physical ({}) and digital ({}) product mapping.", mappingRequestDto.getPhysicalProductId(),
				mappingRequestDto.getDigitalProductId());
		return "Updated successfully.";
	}

	@Override
	public List<SuggestionDto> getMappedProducts(Pageable pageable) {
		log.info("Fetching mapped products.");
		return digitalProductRepository.findMappedPhysicalAndDigitalProducts(pageable);
	}

	@Override
	public List<SuggestionDto> getUnMappedProducts(Pageable pageable) {
		log.info("Fetching unmapped products.");
		return digitalProductRepository.findUnmappedPhysicalAndDigitalProducts(pageable);
	}

}
