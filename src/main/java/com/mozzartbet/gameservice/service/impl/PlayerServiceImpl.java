package com.mozzartbet.gameservice.service.impl;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.repository.PlayerRepository;
import com.mozzartbet.gameservice.service.PlayerService;
import com.mozzartbet.gameservice.service.dto.PlayerSearchRequest;
import com.mozzartbet.gameservice.service.dto.PlayerSearchResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {
  @Autowired
  private PlayerRepository playerRepository;


  @Override
  public PlayerSearchResponse searchPlayers(PlayerSearchRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int save(Player entity) {
    return playerRepository.insert(entity);
  }

  @Override
  public Map<Long, Player> getTeamPlayers(Long teamId) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Player getPlayer(Long playerId) {
    return playerRepository.getById(playerId);
  }

  @Override
  public Player getPlayerCached(Long playerId) {
    // TODO Auto-generated method stub
    return null;
  }

}
