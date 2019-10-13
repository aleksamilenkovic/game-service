package com.gameservice.domain;

import java.time.LocalDateTime;

public interface BaseEntity {
	public Long getId();

	public void setId(Long id);

	public LocalDateTime getCreatedOn();

	public void setCreatedOn(LocalDateTime createdOn);

	public LocalDateTime getModifiedOn();

	public void setModifiedOn(LocalDateTime modifiedOn);
}
