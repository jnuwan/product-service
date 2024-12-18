package com.microservice.product_service.dto;

import com.microservice.product_service.model.DigitalProduct;
import com.microservice.product_service.model.PhysicalProduct;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuggestionDto {

	private PhysicalProduct physicalProduct;

	private DigitalProduct digitalProduct;

	public SuggestionDto(DigitalProduct digitalProduct) {
		super();
		this.digitalProduct = digitalProduct;
	}

}
