package com.mozzartbet.gameservice.service;

import java.util.List;
import java.util.Map;
import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.service.dto.PlayerSearchRequest;
import com.mozzartbet.gameservice.service.dto.PlayerSearchResponse;

public interface PlayerService {
  // 1. Only java annotations (non-Spring): validation
  // 2. Domain/DTOs = Model or standard collections; requests & responses
  // 3. Dilemma: C/Q separation or minimize roundtrips

  // Inconsistencies
  public List<Player> findPlayersByName(String playerName, Long teamId);

  // Method can evolve while keeping the same signature
  public PlayerSearchResponse searchPlayers(PlayerSearchRequest request);

  public Player save(Player player);

  public Map<Long, Player> getTeamPlayers(Long teamId);

  public Player getPlayer(Long playerId);

  // bad name: cache is impl. detail
  public Player getPlayerCached(Long playerId);
}
