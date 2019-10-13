package com.gameservice.mapper;

import com.gameservice.domain.BaseEntity;

public interface BaseMapper<Entity extends BaseEntity> {
	public long count();

	public Entity getById(Long id);

	public int insert(Entity entity);

	public int update(Entity entity);

	public int deleteById(Long id);

	public default int save(Entity entity) {
		return entity.getId() != null ? update(entity) : insert(entity);
	}
}
