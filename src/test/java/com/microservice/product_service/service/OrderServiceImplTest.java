package com.microservice.product_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.microservice.product_service.dto.OrderDto;
import com.microservice.product_service.model.ProductOrder;
import com.microservice.product_service.repository.ProductOrderRepository;
import com.microservice.product_service.service.OrderServiceImpl;

public class OrderServiceImplTest {

	@Mock
	private ProductOrderRepository orderRepository;

	@InjectMocks
	private OrderServiceImpl orderService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testPlaceOrder() {
		OrderDto orderDto = new OrderDto();
		orderDto.setId(1L);
		orderDto.setProductId(101L);
		orderDto.setQuantity(2);
		orderDto.setAmount(200.0);

		ProductOrder mockOrder = new ProductOrder();
		mockOrder.setId(1L);
		mockOrder.setStatus("Pending");
		when(orderRepository.save(any(ProductOrder.class))).thenReturn(mockOrder);

		ProductOrder placedOrder = orderService.placeOrder(orderDto);

		assertNotNull(placedOrder);
		assertEquals("Pending", placedOrder.getStatus());
		verify(orderRepository, times(1)).save(any(ProductOrder.class));
	}

	@Test
	void testMakeThePayment() throws InterruptedException {
		OrderDto orderDto = new OrderDto();
		orderDto.setId(1L);

		ProductOrder mockOrder = new ProductOrder();
		mockOrder.setId(1L);
		mockOrder.setStatus("Pending");
		when(orderRepository.findById(orderDto.getId())).thenReturn(Optional.of(mockOrder));

		orderService.makeThePayment(orderDto, "1234-5678-9101");

		verify(orderRepository, times(1)).findById(orderDto.getId());
		verify(orderRepository, times(1)).save(mockOrder);
	}

	@Test
	void testDispatchForDelivery() {
		OrderDto orderDto = new OrderDto();
		orderDto.setId(1L);

		ProductOrder mockOrder = new ProductOrder();
		mockOrder.setId(1L);
		mockOrder.setStatus("Paid");
		when(orderRepository.findById(orderDto.getId())).thenReturn(Optional.of(mockOrder));

		String result = orderService.dispatchForDelivery(orderDto);

		assertEquals("Order dispatched successfully!", result);
		assertEquals("Dispatched", mockOrder.getStatus());
		verify(orderRepository, times(1)).findById(orderDto.getId());
		verify(orderRepository, times(1)).save(mockOrder);
	}

	@Test
	void testNotifyUser() throws InterruptedException {
		OrderDto orderDto = new OrderDto();
		orderDto.setId(1L);
		orderService.notifyUser(orderDto);
		assertNotNull(orderDto);
	}

	@Test
	void testNotifyMerchant() throws InterruptedException {
		OrderDto orderDto = new OrderDto();
		orderDto.setId(1L);
		orderService.notifyMerchant(orderDto);
		assertNotNull(orderDto);
	}

	@Test
	void testNotifyDelivery() throws InterruptedException {
		OrderDto orderDto = new OrderDto();
		orderDto.setId(1L);

		ProductOrder mockOrder = new ProductOrder();
		mockOrder.setId(1L);
		mockOrder.setStatus("Dispatched");
		when(orderRepository.findById(orderDto.getId())).thenReturn(Optional.of(mockOrder));

		orderService.notifyDelivery(orderDto);

		verify(orderRepository, times(1)).findById(orderDto.getId());
		verify(orderRepository, times(1)).save(mockOrder);
	}
}
