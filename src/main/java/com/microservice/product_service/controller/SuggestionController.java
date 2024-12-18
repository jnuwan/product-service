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

import com.microservice.product_service.dto.SuggestionDto;
import com.microservice.product_service.service.SuggestionService;

import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/v1/suggestion")
@Slf4j
public class SuggestionController {

	@Autowired
	private SuggestionService suggestionService;

	/**
	 * This provides automatic mapping suggestions when digital and physical
	 * product's EANs match
	 * 
	 * @param page
	 * @param size
	 * @return
	 */
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<SuggestionDto> getAutoSuggestProducts(@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "3") int size) {
		log.info("Received request to fetch auto suggessted products. Page is {}, size is {}", page, size);
		Pageable pageable = PageRequest.of(page, size);
		return suggestionService.getAutoSuggestProducts(pageable);
	}

}
