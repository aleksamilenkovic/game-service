package com.mozzartbet.gameservice.service.impl;

import java.util.List;
import java.util.Map;
import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.service.PlayerService;
import com.mozzartbet.gameservice.service.dto.PlayerSearchRequest;
import com.mozzartbet.gameservice.service.dto.PlayerSearchResponse;

public class PlayerServiceImpl implements PlayerService {

  @Override
  public List<Player> findPlayersByName(String playerName, Long teamId) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public PlayerSearchResponse searchPlayers(PlayerSearchRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Player save(Player player) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<Long, Player> getTeamPlayers(Long teamId) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Player getPlayer(Long playerId) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Player getPlayerCached(Long playerId) {
    // TODO Auto-generated method stub
    return null;
  }

}
