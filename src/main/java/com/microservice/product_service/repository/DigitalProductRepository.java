package com.microservice.product_service.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.microservice.product_service.dto.ProductDto;
import com.microservice.product_service.dto.SuggestionDto;
import com.microservice.product_service.model.DigitalProduct;

@Repository
public interface DigitalProductRepository extends CrudRepository<DigitalProduct, Long> {

	List<DigitalProduct> findByEan(String ean);

	@Query("SELECT new com.microservice.product_service.dto.ProductDto(d.id, d.name, d.description, d.ean, d.url, d.price, 'Digital Product') "
			+ "FROM DigitalProduct d WHERE d.name LIKE %:query% OR d.description LIKE %:query% ")
	List<ProductDto> searchDigitalProducts(@Param("query") String query, Pageable pageable);

	@Query("SELECT new com.microservice.product_service.dto.SuggestionDto(p,d) "
			+ "FROM PhysicalProduct p JOIN DigitalProduct d ON p.ean = d.ean AND d.physicalProduct IS NULL")
	List<SuggestionDto> findSuggestedPhysicalAndDigitalProducts(Pageable pageable);

	@Query("SELECT new com.microservice.product_service.dto.SuggestionDto(p,d) "
			+ "FROM PhysicalProduct p JOIN DigitalProduct d ON p.productId = d.physicalProduct.productId AND d.physicalProduct IS NOT NULL")
	List<SuggestionDto> findMappedPhysicalAndDigitalProducts(Pageable pageable);

	@Query("SELECT new com.microservice.product_service.dto.SuggestionDto(d) "
			+ "FROM DigitalProduct d WHERE d.physicalProduct IS NULL")
	List<SuggestionDto> findUnmappedPhysicalAndDigitalProducts(Pageable pageable);

}
