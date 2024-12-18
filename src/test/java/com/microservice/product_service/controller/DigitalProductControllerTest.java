package com.microservice.product_service.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.product_service.controller.DigitalProductController;
import com.microservice.product_service.dto.DigitalProductDto;
import com.microservice.product_service.model.DigitalProduct;
import com.microservice.product_service.model.PhysicalProduct;
import com.microservice.product_service.service.DigitalProductService;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(DigitalProductController.class)
public class DigitalProductControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private DigitalProductService digitalProductService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void testGetAllPhysicalProducts() throws Exception {
		PhysicalProduct physicalProduct = new PhysicalProduct();
		DigitalProduct product1 = new DigitalProduct(1L, "12345", "Product 1", "http://product1.com", "Description 1",
				99.99, physicalProduct);
		DigitalProduct product2 = new DigitalProduct(2L, "67890", "Product 2", "http://product2.com", "Description 2",
				59.99, physicalProduct);
		List<DigitalProduct> products = Arrays.asList(product1, product2);

		when(digitalProductService.searchAll()).thenReturn(products);

		mockMvc.perform(get("/v1/digitalproduct")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(product1.getId()))
				.andExpect(jsonPath("$[0].ean").value(product1.getEan()))
				.andExpect(jsonPath("$[0].name").value(product1.getName()))
				.andExpect(jsonPath("$[1].id").value(product2.getId()))
				.andExpect(jsonPath("$[1].ean").value(product2.getEan()))
				.andExpect(jsonPath("$[1].name").value(product2.getName()));

		verify(digitalProductService, times(1)).searchAll();
	}

	@Test
	public void testGetProductByEan() throws Exception {
		PhysicalProduct physicalProduct = new PhysicalProduct();
		DigitalProduct product = new DigitalProduct(1L, "12345", "Product 1", "http://product1.com", "Description 1",
				99.99, physicalProduct);

		when(digitalProductService.searchById(1L)).thenReturn(product);

		mockMvc.perform(get("/v1/digitalproduct/1")).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(product.getId())).andExpect(jsonPath("$.ean").value(product.getEan()))
				.andExpect(jsonPath("$.name").value(product.getName()));

		verify(digitalProductService, times(1)).searchById(1L);
	}

	@Test
	public void testCreateProduct() throws Exception {
		PhysicalProduct physicalProduct = new PhysicalProduct();
		DigitalProductDto productDto = new DigitalProductDto("12345", "Product 1", "http://product1.com",
				"Description 1", 99.99);
		DigitalProduct createdProduct = new DigitalProduct(1L, "12345", "Product 1", "http://product1.com",
				"Description 1", 99.99, physicalProduct);

		when(digitalProductService.saveOrUpdate(productDto)).thenReturn(createdProduct);

		mockMvc.perform(post("/v1/digitalproduct").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(productDto))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(createdProduct.getId()))
				.andExpect(jsonPath("$.ean").value(createdProduct.getEan()))
				.andExpect(jsonPath("$.name").value(createdProduct.getName()));

		verify(digitalProductService, times(1)).saveOrUpdate(productDto);
	}

	@Test
	public void testUpdateProduct() throws Exception {
		PhysicalProduct physicalProduct = new PhysicalProduct();
		DigitalProductDto productDto = new DigitalProductDto("12345", "Updated Product", "http://updatedproduct.com",
				"Updated Description", 199.99);
		DigitalProduct updatedProduct = new DigitalProduct(1L, "12345", "Updated Product", "http://updatedproduct.com",
				"Updated Description", 199.99, physicalProduct);

		when(digitalProductService.saveOrUpdate(productDto)).thenReturn(updatedProduct);

		mockMvc.perform(put("/v1/digitalproduct").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(productDto))).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(updatedProduct.getId()))
				.andExpect(jsonPath("$.ean").value(updatedProduct.getEan()))
				.andExpect(jsonPath("$.name").value(updatedProduct.getName()));

		verify(digitalProductService, times(1)).saveOrUpdate(productDto);
	}

	@Test
	public void testDeleteById() throws Exception {
		Long productId = 1L;
		doNothing().when(digitalProductService).delete(productId);
		mockMvc.perform(delete("/v1/digitalproduct/{productId}", productId)).andExpect(status().isNoContent());

		verify(digitalProductService, times(1)).delete(productId);
	}
}
