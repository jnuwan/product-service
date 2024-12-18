package com.microservice.product_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

	private Long id;

	@NotNull(message = "Product ID is mandatory")
	private Long productId;

	@Min(1)
	@NotNull(message = "Quentity is mandatory")
	private int quantity;

	@Min(0)
	private Double amount;

}
