package com.microservice.product_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.product_service.dto.DigitalProductDto;
import com.microservice.product_service.model.DigitalProduct;
import com.microservice.product_service.service.DigitalProductService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/v1/digitalproduct")
@Slf4j
public class DigitalProductController {

	@Autowired
	private DigitalProductService digitalProductService;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<DigitalProduct> getAllDigitalProducts() {
		log.info("Received request to fetch all Digital products.");
		return digitalProductService.searchAll();
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public DigitalProduct getProductByEan(@PathVariable("id") Long id) {
		log.info("Received request to fetch Digital product by {} id.", id);
		return digitalProductService.searchById(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public DigitalProduct create(@Valid @RequestBody DigitalProductDto digitalProductDto) {
		log.info("Received request to create a Digital product.");
		return digitalProductService.saveOrUpdate(digitalProductDto);
	}

	@PutMapping
	public DigitalProduct update(@Valid @RequestBody DigitalProductDto digitalProduct) {
		log.info("Received request to update {} Digital product.", digitalProduct.getId());
		return digitalProductService.saveOrUpdate(digitalProduct);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{productId}")
	public void deleteById(@PathVariable("productId") Long productId) {
		log.info("Received request to delete {} Digital product.", productId);
		digitalProductService.delete(productId);
	}

}
