package com.mozzartbet.gameservice.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.mapper.PlayerMapper;

@Repository
public class PlayerRepository implements BaseRepository<Player> {
  @Autowired
  private PlayerMapper playerMapper;

  @Override
  public long count() {
    return playerMapper.count();
  }

  @Override
  public Player getById(Long id) {
    return playerMapper.getById(id);
  }

  public Player getByPlayerIdAndTeamId(String playerId, String teamId) {
    return playerMapper.getByPlayerIdAndTeamId(playerId, teamId);
  }

  @Override
  public int insert(Player entity) {

    if (entity == null || entity.getTeam() == null)
      return 0;
    Player player = getByPlayerIdAndTeamId(entity.getPlayerId(), entity.getTeam().getTeamId());
    return player != null ? 1 : playerMapper.save(entity);

    // return playerMapper.save(entity);
  }

  @Override
  public int update(Player entity) {
    return playerMapper.update(entity);
  }

  @Override
  public int deleteById(Long id) {
    return playerMapper.deleteById(id);
  }

}
