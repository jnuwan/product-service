package com.microservice.product_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.microservice.product_service.dto.DigitalProductDto;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "digital_product")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DigitalProduct extends BaseModel {

	private static final long serialVersionUID = 8015804150552795545L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String ean;
	private String name;
	private String url;
	private String description;
	private Double price;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = true)
	@JsonIgnore
	private PhysicalProduct physicalProduct;

	public void update(DigitalProductDto digitalProductDto) {
		this.ean = digitalProductDto.getEan();
		this.name = digitalProductDto.getName();
		this.url = digitalProductDto.getUrl();
		this.description = digitalProductDto.getDescription();
		this.price = digitalProductDto.getPrice();
	}

}
