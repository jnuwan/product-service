package com.microservice.product_service.controller;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.microservice.product_service.dto.OrderDto;
import com.microservice.product_service.model.ProductOrder;
import com.microservice.product_service.service.OrderService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/v1/order")
@Slf4j
public class OrderController {

	@Autowired
	private OrderService orderService;

	/**
	 * Place a new order
	 * 
	 * @param orderDto
	 * @return
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public OrderDto placeOrder(@RequestBody OrderDto orderDto) {
		log.info("Received request for order placement for {} product.", orderDto.getProductId());
		ProductOrder order = orderService.placeOrder(orderDto);
		orderDto.setId(order.getId());
		log.info("Order placed successfully and order id is {}.", order.getId());
		return orderDto;
	}

	/**
	 * Hanlde payment and track the order status change. Then notify FE as Server
	 * sent events (SSE)
	 * 
	 * @param orderDto
	 * @return
	 */
	@PostMapping("/payment")
	public SseEmitter simulateOrderProcess(@RequestBody OrderDto orderDto) {
		SseEmitter sseEmitter = new SseEmitter();

		new Thread(() -> {
			try {
				sseEmitter.send("{\"orderId\": " + orderDto.getId() + ", \"status\": \"Pending\"}");

				log.info("Handling the payment for {} order id.", orderDto.getId());
				orderService.makeThePayment(orderDto, "1234567890");
				sseEmitter.send("{\"orderId\": " + orderDto.getId() + ", \"status\": \"Paid\"}");

				log.info("Handling user notification for order id {}", orderDto.getId());
				orderService.notifyUser(orderDto);
				sseEmitter.send("{\"orderId\": " + orderDto.getId() + ", \"status\": \"User Notified\"}");

				log.info("Handling merchant notification for order id {}", orderDto.getId());
				orderService.notifyMerchant(orderDto);
				sseEmitter.send("{\"orderId\": " + orderDto.getId() + ", \"status\": \"Merchant Notified\"}");

				sseEmitter.complete();
			} catch (InterruptedException e) {
				log.error("Interrupted Exception during async order processing: {}", e.getMessage());
				sseEmitter.completeWithError(e);
			} catch (IOException e) {
				log.error("IO Exception during async order processing: {}", e.getMessage());
				sseEmitter.completeWithError(e);
			}
		}).start();
		return sseEmitter;
	}

	/**
	 * Dispatch the order
	 * 
	 * @param orderDto
	 * @return
	 */
	@PostMapping("/dispatch")
	@ResponseStatus(HttpStatus.CREATED)
	public OrderDto dispatchForDelivery(@Valid @RequestBody OrderDto orderDto) {
		log.info("Received request for dispatch for order id {}.", orderDto.getId());
		String deliveryStatus = orderService.dispatchForDelivery(orderDto);
		log.info("Order with order id {} dispatched successfully.", orderDto.getId());
		simulateDispatchForDelivery(orderDto);
		return orderDto;
	}

	/**
	 * SImulate the order dispatch and send notification as Server sent event (SSE)
	 * to front end
	 * 
	 * @param orderDto
	 * @return
	 */
	private SseEmitter simulateDispatchForDelivery(OrderDto orderDto) {
		SseEmitter sseEmitter = new SseEmitter();
		new Thread(() -> {
			try {
				// Send SSE event for dispatch
				sseEmitter.send("{\"orderId\": " + orderDto.getId() + ", \"status\": \"Dispatched\"}");

				// Handle delivery notification
				log.info("Handling delivery notification for order id {}", orderDto.getId());
				orderService.notifyDelivery(orderDto);
				sseEmitter.send("{\"orderId\": " + orderDto.getId() + ", \"status\": \"Delivery Notified\"}");

				sseEmitter.complete();
			} catch (InterruptedException e) {
				log.error("Interrupted Exception during async order dispatch: {}", e.getMessage());
				sseEmitter.completeWithError(e);
			} catch (IOException e) {
				log.error("IO Exception during async order dispatch: {}", e.getMessage());
				sseEmitter.completeWithError(e);
			} finally {
				sseEmitter.complete();
			}
		}).start();
		return sseEmitter;
	}

	/**
	 * Listing to the order status change and notify to the FE user
	 * 
	 * @param orderId
	 * @return
	 */
	@GetMapping("/{orderId}/status")
	public SseEmitter listenForOrderStatus(@PathVariable Long orderId) {
		SseEmitter sseEmitter = new SseEmitter();
		new Thread(() -> {
			try {
				ProductOrder order = orderService.fetchOrder(orderId);
				TimeUnit.SECONDS.sleep(1);
				sseEmitter.send("{\"orderId\": " + orderId + ", \"status\": \"" + order.getStatus() + "\"}",
						MediaType.APPLICATION_JSON);
				sseEmitter.complete();
			} catch (InterruptedException e) {
				log.error("Interrupted Exception during async order dispatch: {}", e.getMessage());
				sseEmitter.completeWithError(e);
				sseEmitter.completeWithError(e);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				sseEmitter.complete();
			}
		}).start();
		return sseEmitter;
	}

//	/**
//	 * Simulate the order process
//	 * @param orderDto
//	 * @return
//	 */
//	private OrderDto simulateOrderProcesso(@Valid @RequestBody OrderDto orderDto) {
//		ProductOrder order = orderService.placeOrder(orderDto);
//		orderDto.setId(order.getId());
//		log.info("Order placed successfully and order id is {}.", order.getId());
//		try {
//			log.info("Handling the payment for {} order id.", order.getId());
//			orderService.makeThePayment(orderDto, "1234567890");
//			log.info("Handling user notification for {} order id.", order.getId());
//			orderService.notifyUser(orderDto);
//			log.info("Handling merchant notification for {} order id.", order.getId());
//			orderService.notifyMerchant(orderDto);
//		}
//		catch (InterruptedException e) {
//			log.error("Exception when async running for order placement. exception - {}", e.getMessage());
//		}
//		return orderDto;
//	}
//	
//	//@PostMapping("/dispatch")
//	//@ResponseStatus(HttpStatus.CREATED)
//	public String simulateDispatchForDeliveryo(@Valid @RequestBody OrderDto orderDto) {
//		log.info("Received request for order dispatch for {} order id.", orderDto.getId());
//		String deliveryStatus =  orderService.dispatchForDelivery(orderDto);
//		try {
//			log.info("Handling dilivery notification for {} order id.", orderDto.getId());
//			orderService.notifyDelivery(orderDto);
//		}
//		catch (InterruptedException e) {
//			log.error("Exception when async running for order dispatch. exception - {}", e.getMessage());
//		}
//		return deliveryStatus;
//	}
}
