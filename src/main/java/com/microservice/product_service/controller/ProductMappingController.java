package com.microservice.product_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.product_service.dto.ProductMappingRequestDto;
import com.microservice.product_service.dto.SuggestionDto;
import com.microservice.product_service.service.ProductMappingService;
import com.microservice.product_service.service.SuggestionService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/v1/productmapping")
@Slf4j
public class ProductMappingController {

	@Autowired
	private ProductMappingService productMappingService;

	@Autowired
	private SuggestionService suggestionService;

	/**
	 * Filter products based on it's mapping. Mapping would be mapped, unmapped and
	 * suggest. By default this return mapping suggested products.
	 * 
	 * @param maptype
	 * @param page
	 * @param size
	 * @return
	 */
	@GetMapping("/{maptype}")
	@ResponseStatus(HttpStatus.OK)
	public List<SuggestionDto> getFilterProducts(@PathVariable String maptype,
			@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") int size) {
		log.info("Received request to filter products by {} mapping type. Page is {}, size is {}", maptype, page, size);
		Pageable pageable = PageRequest.of(page, size);
		if (maptype.equals("mapped")) {
			return productMappingService.getMappedProducts(pageable);
		} else if (maptype.equals("unmapped")) {
			return productMappingService.getUnMappedProducts(pageable);
		}
		return suggestionService.getAutoSuggestProducts(pageable);
	}

	/**
	 * Create mapping between digital and physical product. Before make the mapping
	 * validate the user inputs
	 * 
	 * @param mappingRequestDto
	 * @return
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public String createMapping(@Valid @RequestBody ProductMappingRequestDto mappingRequestDto) {
		log.info("Received request to create mapping for {} degital product and {} physical product",
				mappingRequestDto.getDigitalProductId(), mappingRequestDto.getPhysicalProductId());
		return productMappingService.handleProductMapping(mappingRequestDto);
	}

}
