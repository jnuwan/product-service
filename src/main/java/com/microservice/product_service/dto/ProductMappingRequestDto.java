package com.microservice.product_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductMappingRequestDto {

	@NotNull(message = "Physical product is mandatory")
	private Long physicalProductId;

	@NotNull(message = "Digital product is mandatory")
	private Long digitalProductId;

}
