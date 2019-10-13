package com.gameservice.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gameservice.domain.Player;
import com.gameservice.domain.Team;
import com.gameservice.mapper.PlayerMapper;
import com.gameservice.service.dto.PlayerSearchRequest;

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

	public List<Player> getPlayersForTeam(Long teamId) {
		return playerMapper.getPlayersForTeam(teamId);
	}

	public List<Team> getSeasonTeamsAndPlayers(int year) {
		return playerMapper.getSeasonTeamsAndPlayers(year);
	}

	@Override
	public int insert(Player entity) {
		if (entity == null || entity.getTeam() == null)
			return 0;
		// Player player = getByPlayerIdAndTeamId(entity.getPlayerId(),
		// entity.getTeam().getTeamId());
		return playerMapper.save(entity);
	}

	@Override
	public int update(Player entity) {
		return playerMapper.update(entity);
	}

	@Override
	public int deleteById(Long id) {
		return playerMapper.deleteById(id);
	}

	public Player getPlayersInfo(String playerId) {
		return playerMapper.getInfosForPlayer(playerId);
	}

	public List<Player> searchPlayers(@Param("request") PlayerSearchRequest request) {
		return playerMapper.searchPlayers(request);
	}

	public int updateOptimistic(Player player, LocalDateTime expectedModifiedOn) {
		return playerMapper.updateOptimistic(player, expectedModifiedOn);
	}

	public List<Player> findPlayersByName(@Param("playerName") String playerName, @Param("teamId") String teamId) {
		return playerMapper.findPlayersByName(playerName, teamId);
	}
}
