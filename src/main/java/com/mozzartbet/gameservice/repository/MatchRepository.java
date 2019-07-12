package com.mozzartbet.gameservice.repository;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.Quarter;
import com.mozzartbet.gameservice.mapper.MatchMapper;

public class MatchRepository implements BaseRepository<Match> {
  @Autowired
  private MatchMapper matchMapper;

  @Override
  public long count() {
    return matchMapper.count();
  }

  @Override
  public Match getById(Long id) {
    return matchMapper.getById(id);
  }

  public Match getByMatchId(String matchId) {
    return matchMapper.getByMatchId(matchId);
  }

  @Override
  public int insert(Match entity) {
    if (entity == null)
      return 0;
    Match player = getByMatchId(entity.getMatchId());
    return player != null ? 1 : matchMapper.save(entity);
  }

  @Override
  public int update(Match entity) {
    return matchMapper.update(entity);
  }

  @Override
  public int deleteById(Long id) {
    return matchMapper.deleteById(id);
  }


  public List<Quarter> getQuartersByMatchId(String matchId) {
    return matchMapper.getQuartersByMatchId(matchId);
  }

}
