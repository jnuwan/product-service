package com.microservice.product_service.service;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.microservice.product_service.dto.OrderDto;
import com.microservice.product_service.model.ProductOrder;
import com.microservice.product_service.repository.ProductOrderRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

	@Autowired
	private ProductOrderRepository orderRepository;

	@Override
	public ProductOrder placeOrder(OrderDto orderDto) {
		log.info("Prepare to place an order for {} product id.", orderDto.getProductId());
		ProductOrder order = new ProductOrder();
		BeanUtils.copyProperties(orderDto, order);
		order.setStatus("Pending");
		log.info("Placing order for {} product id.", orderDto.getProductId());
		return orderRepository.save(order);
	}

	@Override
	public ProductOrder fetchOrder(Long id) {
		log.info("Fetching order by {} product id.", id);
		return orderRepository.findById(id).get();
	}
	
	@Override
	public void makeThePayment(OrderDto orderDto, String cardNumber) throws InterruptedException {
		log.info("Prepare to make the payment for {} order id.", orderDto.getId());
		ProductOrder order = orderRepository.findById(orderDto.getId()).orElseThrow();
		Thread.sleep(10000L);
		order.setStatus("Paid");
		orderRepository.save(order);
		log.info("Made the payment for {} order id. Current time is {}", orderDto.getId(), new Date());
	}

	@Override
	public void notifyUser(OrderDto orderDto) throws InterruptedException {
		log.info("Notifing to the user for {} order id.", orderDto.getId());
		Thread.sleep(10000L);
		log.info("Notified user for {} order id.", orderDto.getId());
	}

	@Override
	public void notifyMerchant(OrderDto orderDto) throws InterruptedException {
		log.info("Notifing to the merchant for {} order id.", orderDto.getId());
		Thread.sleep(10000L);
		log.info("Notified merchant for {} order id.", orderDto.getId());
	}

	@Override
	public String dispatchForDelivery(OrderDto orderDto) {
		log.info("Dispatching {} order id.", orderDto.getId());
		ProductOrder order = orderRepository.findById(orderDto.getId()).orElseThrow();
		order.setStatus("Dispatched");
		orderRepository.save(order);
		log.info("Order {} dispatched for client. Current time is {}", orderDto.getId(), new Date());
		return "Order dispatched successfully!";
	}

	@Override
	@Async("orderAsyncTaskExecutor")
	public void notifyDelivery(OrderDto orderDto) throws InterruptedException {
		log.info("Notifing {} order id to client for delivery.", orderDto.getId());
		ProductOrder order = orderRepository.findById(orderDto.getId()).orElseThrow();
		Thread.sleep(10000L);
		order.setStatus("Delivered");
		orderRepository.save(order);
		log.info("Notified {} order id to client for delivery.. Current time is {}", orderDto.getId(), new Date());
	}

}
