package com.mozzartbet.gameservice.repository;

import com.mozzartbet.gameservice.domain.BaseEntity;

public interface BaseRepository<Entity extends BaseEntity> {
  public long count();

  public Entity getById(Long id);

  public int insert(Entity entity);

  public int update(Entity entity);

  public int deleteById(Long id);

}
