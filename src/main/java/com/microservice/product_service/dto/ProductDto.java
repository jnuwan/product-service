package com.microservice.product_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

	private Long id;
	private String ean;
	private String name;
	private String url;
	private String description;
	private Double price;
	private String sku;
	private Double quantity;
	private String colour;
	private String size;
	private String productType;

	public ProductDto(Long id, String name, String description, String ean, String productType) {
		super();
		this.id = id;
		this.ean = ean;
		this.name = name;
		this.description = description;
		this.productType = productType;
	}

	public ProductDto(Long id, String name, String description, String ean, String url, Double price,
			String productType) {
		super();
		this.id = id;
		this.ean = ean;
		this.name = name;
		this.url = url;
		this.price = price;
		this.description = description;
		this.productType = productType;
	}

}
