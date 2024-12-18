package com.microservice.product_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.microservice.product_service.dto.SuggestionDto;
import com.microservice.product_service.repository.DigitalProductRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SuggestionServiceImpl implements SuggestionService {

	@Autowired
	private DigitalProductRepository digitalProductRepository;

	@Override
	public List<SuggestionDto> getAutoSuggestProducts(Pageable pageable) {
		log.info("Fetching auto suggested products.");
		return digitalProductRepository.findSuggestedPhysicalAndDigitalProducts(pageable);
	}

}
