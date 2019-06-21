package com.mozzartbet.gameservice.mapper;

public interface BaseMapper<Entity> {
  public long count();

  public Entity getById(String id);

  public int save(Entity entity);

  public int update(Entity entity);

  public int deleteById(Long id);
}
