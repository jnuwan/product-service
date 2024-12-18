package com.microservice.product_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.microservice.product_service.dto.DigitalProductDto;
import com.microservice.product_service.exception.NoProductExistsException;
import com.microservice.product_service.model.DigitalProduct;
import com.microservice.product_service.model.PhysicalProduct;
import com.microservice.product_service.repository.DigitalProductRepository;
import com.microservice.product_service.service.DigitalProductServiceImpl;

public class DigitalProductServiceImplTest {

	@Mock
	private DigitalProductRepository digitalProductRepository;

	@InjectMocks
	private DigitalProductServiceImpl digitalProductService;

	public DigitalProductServiceImplTest() {
		MockitoAnnotations.openMocks(this);
	}

	PhysicalProduct physicalProduct = new PhysicalProduct();

	@Test
	void testSearchAll() {
		DigitalProduct digitalProduct1 = new DigitalProduct(1L, "12345", "Product 1", "http://product1.com",
				"Description 1", 99.99, physicalProduct);

		DigitalProduct digitalProduct2 = new DigitalProduct(2L, "67890", "Product 2", "http://product2.com",
				"Description 2", 59.99, physicalProduct);

		List<DigitalProduct> products = Arrays.asList(digitalProduct1, digitalProduct2);

		when(digitalProductRepository.findAll()).thenReturn(products);

		List<DigitalProduct> result = digitalProductService.searchAll();

		assertEquals(2, result.size());
		assertEquals(digitalProduct1.getName(), result.get(0).getName());
		verify(digitalProductRepository, times(1)).findAll();
	}

	@Test
	void testSearchById_Success() {
		DigitalProduct product = new DigitalProduct(1L, "12345", "Product 1", "http://product1.com", "Description 1",
				99.99, physicalProduct);

		when(digitalProductRepository.findById(1L)).thenReturn(Optional.of(product));

		DigitalProduct result = digitalProductService.searchById(1L);

		assertNotNull(result);
		assertEquals("Product 1", result.getName());
		verify(digitalProductRepository, times(1)).findById(1L);
	}

	@Test
	void testSearchById_NoProductFound() {
		when(digitalProductRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(NoProductExistsException.class, () -> digitalProductService.searchById(1L));

		assertEquals("No digital product found.", exception.getMessage());
		verify(digitalProductRepository, times(1)).findById(1L);
	}

	@Test
	void testSaveOrUpdate_CreateNew() {
		DigitalProductDto dto = new DigitalProductDto("12345", "Product 1", "http://product1.com", "Description 1",
				99.99);
		DigitalProduct savedProduct = new DigitalProduct(1L, "12345", "New Product", "http://example.com",
				"Description", 15.0, physicalProduct);

		when(digitalProductRepository.save(any(DigitalProduct.class))).thenReturn(savedProduct);

		DigitalProduct result = digitalProductService.saveOrUpdate(dto);

		assertNotNull(result);
		assertEquals("New Product", result.getName());
		verify(digitalProductRepository, times(1)).save(any(DigitalProduct.class));
	}

	@Test
	void testSaveOrUpdate_UpdateExisting() {
		DigitalProduct existingProduct = new DigitalProduct(1L, "12345", "Old Product", "http://example.com",
				"Old Description", 10.0, physicalProduct);
		DigitalProductDto dto = new DigitalProductDto(1L, "12345", "Updated Product", "http://example.com",
				"Updated Description", 20.0);

		when(digitalProductRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
		when(digitalProductRepository.save(existingProduct)).thenReturn(existingProduct);

		DigitalProduct result = digitalProductService.saveOrUpdate(dto);

		assertNotNull(result);
		assertEquals("Updated Product", result.getName());
		verify(digitalProductRepository, times(1)).findById(1L);
		verify(digitalProductRepository, times(1)).save(existingProduct);
	}

	@Test
	void testSaveOrUpdate_UpdateNonExisting() {
		DigitalProductDto dto = new DigitalProductDto(1L, "12345", "Updated Product", "http://example.com",
				"Updated Description", 20.0);

		when(digitalProductRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(NoProductExistsException.class,
				() -> digitalProductService.saveOrUpdate(dto));

		assertEquals("No degital product found to update. Please check the the ID", exception.getMessage());
		verify(digitalProductRepository, times(1)).findById(1L);
	}

	@Test
	void testDelete_Success() {
		DigitalProduct product = new DigitalProduct(1L, "12345", "Product1", "http://example.com", "Description1", 10.0,
				physicalProduct);

		when(digitalProductRepository.findById(1L)).thenReturn(Optional.of(product));
		doNothing().when(digitalProductRepository).delete(product);

		assertDoesNotThrow(() -> digitalProductService.delete(1L));
		verify(digitalProductRepository, times(1)).findById(1L);
		verify(digitalProductRepository, times(1)).delete(product);
	}

	@Test
	void testDelete_NoProductFound() {
		when(digitalProductRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(NoProductExistsException.class, () -> digitalProductService.delete(1L));

		assertEquals("No digital product found.", exception.getMessage());
		verify(digitalProductRepository, times(1)).findById(1L);
	}
}
