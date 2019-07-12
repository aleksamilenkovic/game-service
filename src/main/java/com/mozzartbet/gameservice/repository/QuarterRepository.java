package com.mozzartbet.gameservice.repository;

import org.springframework.beans.factory.annotation.Autowired;
import com.mozzartbet.gameservice.domain.Quarter;
import com.mozzartbet.gameservice.mapper.QuarterMapper;

public class QuarterRepository implements BaseRepository<Quarter> {
  @Autowired
  private QuarterMapper quarterMapper;

  @Override
  public long count() {
    return quarterMapper.count();
  }

  @Override
  public Quarter getById(Long id) {
    return quarterMapper.getById(id);
  }

  @Override
  public int insert(Quarter entity) {
    if (entity == null || entity.getMatch() == null)
      return 0;
    Quarter quarter = getById(entity.getId());
    return quarter != null ? 1 : quarterMapper.save(entity);
  }

  @Override
  public int update(Quarter entity) {
    return quarterMapper.update(entity);
  }

  @Override
  public int deleteById(Long id) {
    return quarterMapper.deleteById(id);
  }

}
