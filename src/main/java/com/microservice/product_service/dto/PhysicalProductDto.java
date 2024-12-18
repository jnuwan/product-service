package com.microservice.product_service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhysicalProductDto {

	private Long productId;

	@NotNull(message = "Product SKU is mandatory")
	private String sku;

	@NotNull(message = "Product EAN is mandatory")
	private String ean;

	@NotNull(message = "Product name is mandatory")
	private String name;

	private String description;

	@NotNull(message = "Product price is mandatory")
	@DecimalMin("0.0")
	private Double price;

	@Min(1)
	private Double quantity;

	private String colour;

	private String size;

	public PhysicalProductDto(String sku, String ean, String name, String description, Double price, Double quantity,
			String colour, String size) {
		super();
		this.sku = sku;
		this.ean = ean;
		this.name = name;
		this.description = description;
		this.price = price;
		this.quantity = quantity;
		this.colour = colour;
		this.size = size;
	}

}
