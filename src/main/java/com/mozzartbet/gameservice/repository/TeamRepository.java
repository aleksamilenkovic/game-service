package com.mozzartbet.gameservice.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.mozzartbet.gameservice.domain.Team;
import com.mozzartbet.gameservice.mapper.TeamMapper;

@Repository
public class TeamRepository implements BaseRepository<Team> {
  @Autowired
  private TeamMapper teamMapper;
  @Autowired
  private PlayerRepository playerRepository;

  @Override
  public long count() {
    return teamMapper.count();
  }

  @Override
  public Team getById(Long id) {
    return teamMapper.getById(id);
  }

  public Team getByTeamId(String teamId) {
    return teamMapper.getByTeamId(teamId);
  }

  @Override
  public int insert(Team entity) {
    if (entity == null)
      return 0;
    Team player = getByTeamId(entity.getTeamId());
    return player != null ? 1 : teamMapper.save(entity);
  }

  @Override
  public int update(Team entity) {
    return teamMapper.update(entity);
  }

  @Override
  public int deleteById(Long id) {
    return teamMapper.deleteById(id);
  }

}
