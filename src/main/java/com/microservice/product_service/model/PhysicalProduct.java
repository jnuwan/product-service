package com.microservice.product_service.model;

import com.microservice.product_service.dto.PhysicalProductDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "physical_product")
@NoArgsConstructor
@AllArgsConstructor
public class PhysicalProduct extends BaseModel {

	private static final long serialVersionUID = 8511367096174928210L;

	@Id
	@Column(name = "product_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productId;
	private String sku;
	private String ean;
	private String name;
	private String description;
	private Double price;
	private Double quantity;
	private String colour;
	private String size;

	public void update(PhysicalProductDto physicalProductDto) {
		this.sku = physicalProductDto.getSku();
		this.ean = physicalProductDto.getEan();
		this.name = physicalProductDto.getName();
		this.description = physicalProductDto.getDescription();
		this.price = physicalProductDto.getPrice();
		this.quantity = physicalProductDto.getQuantity();
		this.colour = physicalProductDto.getColour();
		this.size = physicalProductDto.getSize();
	}

}
