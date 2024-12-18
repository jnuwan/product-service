package com.microservice.product_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;

import com.microservice.product_service.dto.SuggestionDto;
import com.microservice.product_service.model.DigitalProduct;
import com.microservice.product_service.model.PhysicalProduct;
import com.microservice.product_service.repository.DigitalProductRepository;
import com.microservice.product_service.service.SuggestionServiceImpl;

public class SuggestionServiceImplTest {

	@Mock
	private DigitalProductRepository digitalProductRepository;

	@InjectMocks
	private SuggestionServiceImpl suggestionService;

	public SuggestionServiceImplTest() {
		MockitoAnnotations.openMocks(this);
	}

	PhysicalProduct physicalProduct = new PhysicalProduct();

	@Test
	void testGetAutoSuggestProducts_Success() {
		Pageable pageable = mock(Pageable.class);
		List<SuggestionDto> suggestions = new ArrayList<>();
		DigitalProduct digitalProduct1 = new DigitalProduct(1L, "12345", "Product 1", "http://product1.com",
				"Description 1", 99.99, physicalProduct);

		DigitalProduct digitalProduct2 = new DigitalProduct(2L, "67890", "Product 2", "http://product2.com",
				"Description 2", 59.99, physicalProduct);

		SuggestionDto suggestion1 = new SuggestionDto(physicalProduct, digitalProduct1);
		SuggestionDto suggestion2 = new SuggestionDto(physicalProduct, digitalProduct2);

		suggestions.add(suggestion1);
		suggestions.add(suggestion2);

		when(digitalProductRepository.findSuggestedPhysicalAndDigitalProducts(pageable)).thenReturn(suggestions);

		List<SuggestionDto> result = suggestionService.getAutoSuggestProducts(pageable);

		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals("Product 1", result.get(0).getDigitalProduct().getName());
		assertEquals("67890", result.get(1).getDigitalProduct().getEan());

		verify(digitalProductRepository, times(1)).findSuggestedPhysicalAndDigitalProducts(pageable);
	}

	@Test
	void testGetAutoSuggestProducts_EmptyResult() {
		Pageable pageable = mock(Pageable.class);
		List<SuggestionDto> suggestions = new ArrayList<>();

		when(digitalProductRepository.findSuggestedPhysicalAndDigitalProducts(pageable)).thenReturn(suggestions);

		List<SuggestionDto> result = suggestionService.getAutoSuggestProducts(pageable);

		assertNotNull(result);
		assertTrue(result.isEmpty());

		verify(digitalProductRepository, times(1)).findSuggestedPhysicalAndDigitalProducts(pageable);
	}

	@Test
	void testGetAutoSuggestProducts_NullResult() {
		Pageable pageable = mock(Pageable.class);

		when(digitalProductRepository.findSuggestedPhysicalAndDigitalProducts(pageable)).thenReturn(null);

		List<SuggestionDto> result = suggestionService.getAutoSuggestProducts(pageable);

		assertNull(result);

		verify(digitalProductRepository, times(1)).findSuggestedPhysicalAndDigitalProducts(pageable);
	}
}
