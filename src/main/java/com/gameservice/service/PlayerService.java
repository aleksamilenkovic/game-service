package com.gameservice.service;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.gameservice.annotation.Loggable;
import com.gameservice.domain.Player;
import com.gameservice.service.dto.PlayerSearchRequest;
import com.gameservice.service.dto.PlayerSearchResponse;

@Loggable(detail = true)
public interface PlayerService {
	// 1. Only java annotations (non-Spring): validation
	// 2. Domain/DTOs = Model or standard collections; requests & responses
	// 3. Dilemma: C/Q separation or minimize roundtrips

	// Inconsistencies
	// public List<Player> findPlayersByName(String playerName, String teamId);

	// Method can evolve while keeping the same signature
	public @NotNull @Valid PlayerSearchResponse searchPlayers(@NotNull @Valid PlayerSearchRequest request);

	public int save(Player player);

	public Map<Long, Player> getTeamPlayers(Long teamId);

	public Player getPlayer(Long playerId);

	// bad name: cache is impl. detail
	public Player getPlayerCached(Long playerId);

	public enum LockType {
		MEMORY, STRIPED, PESSIMISTIC, OPTIMISTIC
	}

	public Player save(Player player, LockType lockType);

	public List<Player> findPlayersByName(@NotNull String playerName, String teamId);

	public enum PlayerDestination {
		CREATED;

		public String getName() {
			return "game-service." + name().toLowerCase();
		}
	}
}
