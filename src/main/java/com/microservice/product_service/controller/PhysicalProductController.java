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

import com.microservice.product_service.dto.PhysicalProductDto;
import com.microservice.product_service.model.PhysicalProduct;
import com.microservice.product_service.service.PhysicalProductService;

import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/v1/physicalproduct")
@Slf4j
public class PhysicalProductController {

	@Autowired
	private PhysicalProductService physicalProductService;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<PhysicalProduct> getAllPhysicalProducts() {
		log.info("Received request to fetch all Physical products.");
		return physicalProductService.searchAll();
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public PhysicalProduct getProductByEan(@PathVariable Long id) {
		log.info("Received request to fetch Physical product by {} id.", id);
		return physicalProductService.searchById(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PhysicalProduct create(@RequestBody PhysicalProductDto physicalProductDto) {
		log.info("Received request to create a Physical product.");
		return physicalProductService.saveOrUpdate(physicalProductDto);
	}

	@PutMapping
	@ResponseStatus(HttpStatus.OK)
	public PhysicalProduct update(@RequestBody PhysicalProductDto physicalProduct) {
		log.info("Received request to update {} Physical product.", physicalProduct.getProductId());
		return physicalProductService.saveOrUpdate(physicalProduct);
	}

	@ResponseStatus(HttpStatus.ACCEPTED)
	@DeleteMapping("/{productId}")
	public void deleteById(@PathVariable Long productId) {
		log.info("Received request to delete {} Physical product.", productId);
		physicalProductService.delete(productId);
	}

}
