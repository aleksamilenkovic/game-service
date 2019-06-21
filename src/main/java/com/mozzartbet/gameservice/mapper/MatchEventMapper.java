package com.mozzartbet.gameservice.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.mozzartbet.gameservice.domain.MatchEvent;

@Mapper
public interface MatchEventMapper extends BaseMapper<MatchEvent> {

  public long count();

  public MatchEvent getById(Long id);

  public int save(MatchEvent entity);

  public int update(MatchEvent entity);

  public int deleteById(Long id);

}
