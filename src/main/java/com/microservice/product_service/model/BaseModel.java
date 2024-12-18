package com.microservice.product_service.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseModel implements Serializable {

	private static final long serialVersionUID = 8843599469693245410L;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", nullable = false, updatable = false)
	@CreatedDate
	private Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified_at", nullable = false)
	@LastModifiedDate
	private Date modifiedAt;

	@Column(name = "requested_by")
	private String requestedBy;

	@Column(name = "modified_by")
	@LastModifiedBy
	private String modifiedBy;

	@Version
	private Integer version;

	@PrePersist
	private void beforePersist() {
		Date currentDate = new Date();
		this.createdAt = currentDate;
		this.modifiedAt = currentDate;
	}

	@PreUpdate
	private void beforeModify() {
		this.modifiedAt = new Date();
	}

	@PreRemove
	private void beforeRemove() {
		this.modifiedAt = new Date();
	}

}
