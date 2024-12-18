package com.microservice.product_service.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservice.product_service.dto.DigitalProductDto;
import com.microservice.product_service.exception.NoProductExistsException;
import com.microservice.product_service.model.DigitalProduct;
import com.microservice.product_service.repository.DigitalProductRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DigitalProductServiceImpl implements DigitalProductService {

	@Autowired
	private DigitalProductRepository digitalProductRepository;

	@Override
	public List<DigitalProduct> searchAll() {
		log.info("Fetching all digital products.");
		return (List<DigitalProduct>) digitalProductRepository.findAll();
	}

	@Override
	public DigitalProduct searchById(Long id) {
		log.info("Fetching digital product by {} product id.", id);
		return digitalProductRepository.findById(id)
				.orElseThrow(() -> new NoProductExistsException("No digital product found."));
	}

	@Override
	public DigitalProduct saveOrUpdate(DigitalProductDto digitalProductDto) {
		DigitalProduct digitalProduct = new DigitalProduct();

		if (digitalProductDto.getId() != null) {
			log.info("Updating digital product by {} product id.", digitalProductDto.getId());
			digitalProduct = digitalProductRepository.findById(digitalProductDto.getId()).orElseThrow(
					() -> new NoProductExistsException("No degital product found to update. Please check the the ID"));
			digitalProduct.update(digitalProductDto);
			return digitalProductRepository.save(digitalProduct);
		}

		log.info("Creating digital product.");
		BeanUtils.copyProperties(digitalProductDto, digitalProduct);
		return digitalProductRepository.save(digitalProduct);
	}

	@Override
	public void delete(Long productId) {
		DigitalProduct digitalProduct = digitalProductRepository.findById(productId)
				.orElseThrow(() -> new NoProductExistsException("No digital product found."));
		log.info("Deleting digital product by {} product id.", productId);
		digitalProductRepository.delete(digitalProduct);
	}

}
