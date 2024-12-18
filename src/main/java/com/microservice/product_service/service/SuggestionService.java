package com.microservice.product_service.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.microservice.product_service.dto.SuggestionDto;

public interface SuggestionService {

	List<SuggestionDto> getAutoSuggestProducts(Pageable pageable);

}
