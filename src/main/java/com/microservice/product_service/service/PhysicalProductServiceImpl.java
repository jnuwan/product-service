package com.microservice.product_service.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservice.product_service.dto.PhysicalProductDto;
import com.microservice.product_service.exception.NoProductExistsException;
import com.microservice.product_service.model.PhysicalProduct;
import com.microservice.product_service.repository.PhysicalProductRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PhysicalProductServiceImpl implements PhysicalProductService {

	@Autowired
	private PhysicalProductRepository physicalProductRepository;

	@Override
	public List<PhysicalProduct> searchAll() {
		log.info("Fetching all physical products.");
		return (List<PhysicalProduct>) physicalProductRepository.findAll();
	}

	@Override
	public PhysicalProduct searchById(Long id) {
		log.info("Fetching physical product by {} product id.", id);
		return physicalProductRepository.findById(id)
				.orElseThrow(() -> new NoProductExistsException("No product found in our store."));
	}

	@Override
	public PhysicalProduct saveOrUpdate(PhysicalProductDto physicalProductDto) {
		PhysicalProduct physicalProduct = new PhysicalProduct();

		if (physicalProductDto.getProductId() != null) {
			log.info("Updating physical product by {} product id.", physicalProductDto.getProductId());
			physicalProduct = physicalProductRepository.findById(physicalProductDto.getProductId()).orElseThrow(
					() -> new NoProductExistsException("No product found to update. Please check the product ID"));
			physicalProduct.update(physicalProductDto);
			return physicalProductRepository.save(physicalProduct);
		}

		log.info("Creating physical product.");
		BeanUtils.copyProperties(physicalProductDto, physicalProduct);
		return physicalProductRepository.save(physicalProduct);
	}

	@Override
	public void delete(Long productId) {
		PhysicalProduct physicalProduct = physicalProductRepository.findById(productId)
				.orElseThrow(() -> new NoProductExistsException("No product found in our store."));
		log.info("Deleting physical product by {} product id.", productId);
		physicalProductRepository.delete(physicalProduct);
	}

}
