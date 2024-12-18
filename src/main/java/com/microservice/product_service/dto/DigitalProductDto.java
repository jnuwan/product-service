package com.microservice.product_service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DigitalProductDto {

	private Long id;

	@NotNull(message = "Product EAN is mandatory")
	private String ean;

	@NotNull(message = "Product name is mandatory")
	private String name;

	private String url;
	private String description;

	@NotNull(message = "Product price is mandatory")
	@DecimalMin("0.0")
	private Double price;

	public DigitalProductDto(String ean, String name, String url, String description, Double price) {
		super();
		this.ean = ean;
		this.name = name;
		this.url = url;
		this.description = description;
		this.price = price;
	}

	public DigitalProductDto(Long id) {
		super();
		this.id = id;
	}

}
