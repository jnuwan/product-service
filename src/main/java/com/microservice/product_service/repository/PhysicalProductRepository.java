package com.microservice.product_service.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.microservice.product_service.dto.ProductDto;
import com.microservice.product_service.model.PhysicalProduct;

@Repository
public interface PhysicalProductRepository extends CrudRepository<PhysicalProduct, Long> {

	List<PhysicalProduct> findByEan(String ean);

	@Query("SELECT new com.microservice.product_service.dto.ProductDto(p.productId, p.name, p.description, p.ean, 'Physical Product') "
			+ "FROM PhysicalProduct p WHERE p.name LIKE %:query% OR p.description LIKE %:query% " + "UNION "
			+ "SELECT new com.microservice.product_service.dto.ProductDto(d.id, d.name, d.description, d.ean, 'Digital Product') "
			+ "FROM DigitalProduct d WHERE d.name LIKE %:query% OR d.description LIKE %:query% ")
	List<ProductDto> searchPhysicalAndDigitalProducts(@Param("query") String query, Pageable pageable);

	@Query("SELECT new com.microservice.product_service.dto.ProductDto(p.productId, p.name, p.description, p.ean, 'Physical Product') "
			+ "FROM PhysicalProduct p WHERE p.name LIKE %:query% OR p.description LIKE %:query% ")
	List<ProductDto> searchPhysicalProducts(@Param("query") String query, Pageable pageable);
}
