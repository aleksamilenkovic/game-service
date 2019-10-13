package com.gameservice.repository;

import java.sql.SQLIntegrityConstraintViolationException;

import com.gameservice.domain.BaseEntity;

public interface BaseRepository<Entity extends BaseEntity> {
	public long count();

	public Entity getById(Long id);

	public int insert(Entity entity) throws SQLIntegrityConstraintViolationException;

	public int update(Entity entity);

	public int deleteById(Long id);

}
