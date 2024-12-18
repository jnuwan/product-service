package com.microservice.product_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;

import com.microservice.product_service.dto.ProductMappingRequestDto;
import com.microservice.product_service.dto.SuggestionDto;
import com.microservice.product_service.exception.NoProductExistsException;
import com.microservice.product_service.model.DigitalProduct;
import com.microservice.product_service.model.PhysicalProduct;
import com.microservice.product_service.repository.DigitalProductRepository;
import com.microservice.product_service.repository.PhysicalProductRepository;
import com.microservice.product_service.service.ProductMappingServiceImpl;

public class ProductMappingServiceImplTest {

	@Mock
	private PhysicalProductRepository physicalProductRepository;

	@Mock
	private DigitalProductRepository digitalProductRepository;

	@InjectMocks
	private ProductMappingServiceImpl productMappingService;

	public ProductMappingServiceImplTest() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testHandleProductMapping_Success() {
		ProductMappingRequestDto requestDto = new ProductMappingRequestDto(1L, 2L);
		PhysicalProduct physicalProduct = new PhysicalProduct();
		physicalProduct.setProductId(1L);
		DigitalProduct digitalProduct = new DigitalProduct();
		digitalProduct.setId(2L);

		when(physicalProductRepository.findById(1L)).thenReturn(Optional.of(physicalProduct));
		when(digitalProductRepository.findById(2L)).thenReturn(Optional.of(digitalProduct));
		when(digitalProductRepository.save(digitalProduct)).thenReturn(digitalProduct);

		String result = productMappingService.handleProductMapping(requestDto);

		assertEquals("Updated successfully.", result);
		assertEquals(physicalProduct, digitalProduct.getPhysicalProduct());
		verify(physicalProductRepository, times(1)).findById(1L);
		verify(digitalProductRepository, times(1)).findById(2L);
		verify(digitalProductRepository, times(1)).save(digitalProduct);
	}

	@Test
	void testHandleProductMapping_NoPhysicalProduct() {
		ProductMappingRequestDto requestDto = new ProductMappingRequestDto(1L, 2L);

		when(physicalProductRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(NoProductExistsException.class,
				() -> productMappingService.handleProductMapping(requestDto));

		assertEquals("No physical product found.", exception.getMessage());
		verify(physicalProductRepository, times(1)).findById(1L);
		verifyNoInteractions(digitalProductRepository);
	}

	@Test
	void testHandleProductMapping_NoDigitalProduct() {
		ProductMappingRequestDto requestDto = new ProductMappingRequestDto(1L, 2L);
		PhysicalProduct physicalProduct = new PhysicalProduct();
		physicalProduct.setProductId(1L);

		when(physicalProductRepository.findById(1L)).thenReturn(Optional.of(physicalProduct));
		when(digitalProductRepository.findById(2L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(NoProductExistsException.class,
				() -> productMappingService.handleProductMapping(requestDto));

		assertEquals("No digital product found.", exception.getMessage());
		verify(physicalProductRepository, times(1)).findById(1L);
		verify(digitalProductRepository, times(1)).findById(2L);
	}

	@Test
	void testGetMappedProducts_Success() {
		Pageable pageable = mock(Pageable.class);
		List<SuggestionDto> mappedProducts = new ArrayList<>();

		PhysicalProduct physicalProduct1 = new PhysicalProduct();
		physicalProduct1.setProductId(1L);
		DigitalProduct digitalProduct1 = new DigitalProduct();
		digitalProduct1.setId(1L);

		PhysicalProduct physicalProduct2 = new PhysicalProduct();
		physicalProduct2.setProductId(2L);
		DigitalProduct digitalProduct2 = new DigitalProduct();
		digitalProduct2.setId(2L);

		mappedProducts.add(new SuggestionDto(physicalProduct1, digitalProduct1));
		mappedProducts.add(new SuggestionDto(physicalProduct2, digitalProduct2));

		when(digitalProductRepository.findMappedPhysicalAndDigitalProducts(pageable)).thenReturn(mappedProducts);

		List<SuggestionDto> result = productMappingService.getMappedProducts(pageable);

		assertEquals(2, result.size());
		assertEquals(1L, result.get(0).getPhysicalProduct().getProductId());
		assertEquals(2L, result.get(1).getDigitalProduct().getId());
		verify(digitalProductRepository, times(1)).findMappedPhysicalAndDigitalProducts(pageable);
	}

	@Test
	void testGetUnMappedProducts_Success() {
		Pageable pageable = mock(Pageable.class);
		List<SuggestionDto> unmappedProducts = new ArrayList<>();

		PhysicalProduct physicalProduct1 = new PhysicalProduct();
		physicalProduct1.setProductId(1L);
		DigitalProduct digitalProduct1 = new DigitalProduct();
		digitalProduct1.setId(1L);

		PhysicalProduct physicalProduct2 = new PhysicalProduct();
		physicalProduct2.setProductId(2L);
		DigitalProduct digitalProduct2 = new DigitalProduct();
		digitalProduct2.setId(2L);

		unmappedProducts.add(new SuggestionDto(physicalProduct1, digitalProduct1));
		unmappedProducts.add(new SuggestionDto(physicalProduct2, digitalProduct2));

		when(digitalProductRepository.findUnmappedPhysicalAndDigitalProducts(pageable)).thenReturn(unmappedProducts);

		List<SuggestionDto> result = productMappingService.getUnMappedProducts(pageable);

		assertEquals(2, result.size());
		assertEquals(1L, result.get(0).getPhysicalProduct().getProductId());
		assertNotNull(result.get(0).getDigitalProduct().getId());
		verify(digitalProductRepository, times(1)).findUnmappedPhysicalAndDigitalProducts(pageable);
	}
}
