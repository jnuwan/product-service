package com.microservice.product_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.product_service.dto.ProductDto;
import com.microservice.product_service.service.ProductSearchService;

import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/v1/searchproduct")
@Slf4j
public class ProductSearchController {

	@Autowired
	private ProductSearchService productSearchService;

	/**
	 * Product search based on the free text from front-end
	 * 
	 * @param query
	 * @param page
	 * @param size
	 * @return
	 */
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<ProductDto> productsSearch(@RequestParam(name = "query") String query,
			@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") int size) {
		log.info("Received request to search product by {} text. Page is {}, size is {}", query, page, size);
		Pageable pageable = PageRequest.of(page, size);
		return productSearchService.searchProductByText(query, pageable);
	}

	/**
	 * Physical Product search based on the free text from front-end
	 * 
	 * @param query
	 * @param page
	 * @param size
	 * @return
	 */
	@GetMapping("/digital")
	@ResponseStatus(HttpStatus.OK)
	public List<ProductDto> searchDigitapProducts(@RequestParam(name = "query") String query,
			@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") int size) {
		log.info("Received request to search digital product by {} text. Page is {}, size is {}", query, page, size);
		Pageable pageable = PageRequest.of(page, size);
		return productSearchService.searchDigitalProductByText(query, pageable);
	}

	/**
	 * Digital Product search based on the free text from front-end
	 * 
	 * @param query
	 * @param page
	 * @param size
	 * @return
	 */
	@GetMapping("/physical")
	@ResponseStatus(HttpStatus.OK)
	public List<ProductDto> searchPhysicalProduct(@RequestParam(name = "query") String query,
			@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") int size) {
		log.info("Received request to search physical product by {} text. Page is {}, size is {}", query, page, size);
		Pageable pageable = PageRequest.of(page, size);
		return productSearchService.searchPhysicalProductByText(query, pageable);
	}

}
