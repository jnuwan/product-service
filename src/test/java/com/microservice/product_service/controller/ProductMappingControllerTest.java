package com.microservice.product_service.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.product_service.controller.ProductMappingController;
import com.microservice.product_service.dto.ProductMappingRequestDto;
import com.microservice.product_service.dto.SuggestionDto;
import com.microservice.product_service.model.DigitalProduct;
import com.microservice.product_service.model.PhysicalProduct;
import com.microservice.product_service.service.ProductMappingService;
import com.microservice.product_service.service.SuggestionService;

@WebMvcTest(ProductMappingController.class)
public class ProductMappingControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductMappingService productMappingService;

	@MockBean
	private SuggestionService suggestionService;

	@Autowired
	private ObjectMapper objectMapper;

	PhysicalProduct physicalProduct = new PhysicalProduct();

	@Test
	public void testGetMappedProducts() throws Exception {
		DigitalProduct digitalProduct1 = new DigitalProduct(1L, "12345", "Product 1", "http://product1.com",
				"Description 1", 99.99, physicalProduct);

		DigitalProduct digitalProduct2 = new DigitalProduct(2L, "67890", "Product 2", "http://product2.com",
				"Description 2", 59.99, physicalProduct);

		SuggestionDto suggestion1 = new SuggestionDto(physicalProduct, digitalProduct1);
		SuggestionDto suggestion2 = new SuggestionDto(physicalProduct, digitalProduct2);

		List<SuggestionDto> suggestions = Arrays.asList(suggestion1, suggestion2);
		Pageable pageable = PageRequest.of(0, 10);

		when(productMappingService.getMappedProducts(pageable)).thenReturn(suggestions);

		mockMvc.perform(get("/v1/productmapping/mapped").param("page", "0").param("size", "10"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].digitalProduct.id").value(suggestion1.getDigitalProduct().getId()))
				.andExpect(jsonPath("$[1].digitalProduct.id").value(suggestion2.getDigitalProduct().getId()));

		verify(productMappingService, times(1)).getMappedProducts(pageable);
	}

	@Test
	public void testGetUnMappedProducts() throws Exception {
		DigitalProduct digitalProduct1 = new DigitalProduct(1L, "12345", "Product 1", "http://product1.com",
				"Description 1", 99.99, physicalProduct);

		DigitalProduct digitalProduct2 = new DigitalProduct(2L, "67890", "Product 2", "http://product2.com",
				"Description 2", 59.99, physicalProduct);

		SuggestionDto suggestion1 = new SuggestionDto(physicalProduct, digitalProduct1);
		SuggestionDto suggestion2 = new SuggestionDto(physicalProduct, digitalProduct2);

		List<SuggestionDto> suggestions = Arrays.asList(suggestion1, suggestion2);
		Pageable pageable = PageRequest.of(0, 10);

		when(productMappingService.getUnMappedProducts(pageable)).thenReturn(suggestions);

		mockMvc.perform(get("/v1/productmapping/unmapped").param("page", "0").param("size", "10"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].digitalProduct.id").value(suggestion1.getDigitalProduct().getId()))
				.andExpect(jsonPath("$[1].digitalProduct.id").value(suggestion2.getDigitalProduct().getId()));

		verify(productMappingService, times(1)).getUnMappedProducts(pageable);
	}

	@Test
	public void testGetAutoSuggestProducts() throws Exception {
		DigitalProduct digitalProduct1 = new DigitalProduct(1L, "12345", "Product 1", "http://product1.com",
				"Description 1", 99.99, physicalProduct);

		DigitalProduct digitalProduct2 = new DigitalProduct(2L, "67890", "Product 2", "http://product2.com",
				"Description 2", 59.99, physicalProduct);

		SuggestionDto suggestion1 = new SuggestionDto(physicalProduct, digitalProduct1);
		SuggestionDto suggestion2 = new SuggestionDto(physicalProduct, digitalProduct2);

		List<SuggestionDto> suggestions = Arrays.asList(suggestion1, suggestion2);
		Pageable pageable = PageRequest.of(0, 10);

		when(suggestionService.getAutoSuggestProducts(pageable)).thenReturn(suggestions);

		mockMvc.perform(get("/v1/productmapping/other").param("page", "0").param("size", "10"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].digitalProduct.id").value(suggestion1.getDigitalProduct().getId()))
				.andExpect(jsonPath("$[1].digitalProduct.id").value(suggestion2.getDigitalProduct().getId()));

		verify(suggestionService, times(1)).getAutoSuggestProducts(pageable);
	}

	@Test
	public void testCreateMapping() throws Exception {
		ProductMappingRequestDto mappingRequestDto = new ProductMappingRequestDto(1L, 2L);

		when(productMappingService.handleProductMapping(mappingRequestDto)).thenReturn("Mapped Successfully");

		mockMvc.perform(post("/v1/productmapping").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(mappingRequestDto))).andExpect(status().isCreated())
				.andExpect(content().string("Mapped Successfully"));

		verify(productMappingService, times(1)).handleProductMapping(mappingRequestDto);
	}
}
