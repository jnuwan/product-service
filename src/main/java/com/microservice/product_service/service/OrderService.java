package com.microservice.product_service.service;

import com.microservice.product_service.dto.OrderDto;
import com.microservice.product_service.model.ProductOrder;

public interface OrderService {

	//Process Order
	ProductOrder placeOrder(OrderDto orderDto);
	
	ProductOrder fetchOrder(Long id);

	//make the payment
	void makeThePayment(OrderDto orderDto, String cardNumber) throws InterruptedException;
	
	//notify user
	void notifyUser(OrderDto orderDto) throws InterruptedException;
	
	//notify to the merchant
	void notifyMerchant(OrderDto orderDto) throws InterruptedException;
	
	//dispatch for delivery
	String dispatchForDelivery(OrderDto orderDto);
	
	//Notify delivery
	void notifyDelivery(OrderDto orderDto) throws InterruptedException;
	
}
