package com.mozzartbet.gameservice.repository;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.mozzartbet.gameservice.domain.MatchEvent;
import com.mozzartbet.gameservice.mapper.MatchEventMapper;

public class MatchEventRepository implements BaseRepository<MatchEvent> {
  @Autowired
  private MatchEventMapper matchEventMapper;

  @Override
  public long count() {
    return matchEventMapper.count();
  }

  @Override
  public MatchEvent getById(Long id) {
    return matchEventMapper.getById(id);
  }

  @Override
  public int insert(MatchEvent entity) {
    if (entity == null)
      return 0;
    return matchEventMapper.save(entity);
  }

  @Override
  public int update(MatchEvent entity) {
    return matchEventMapper.update(entity);
  }

  @Override
  public int deleteById(Long id) {
    return matchEventMapper.deleteById(id);
  }

  public List<MatchEvent> getEventsForQuarter(Long quarterId) {
    return matchEventMapper.getEventsFromQuarterId(quarterId);
  }
}
