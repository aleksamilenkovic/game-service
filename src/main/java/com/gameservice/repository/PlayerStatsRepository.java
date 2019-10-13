package com.gameservice.repository;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gameservice.domain.boxscore.PlayerStats;
import com.gameservice.mapper.PlayerStatsMapper;

@Repository
public class PlayerStatsRepository implements BaseRepository<PlayerStats> {
	@Autowired
	private PlayerStatsMapper playerStatsMapper;

	@Override
	public long count() {
		return playerStatsMapper.count();
	}

	@Override
	public PlayerStats getById(Long id) {
		return playerStatsMapper.getById(id);
	}

	@Override
	public int insert(PlayerStats entity) throws SQLIntegrityConstraintViolationException {
		if (entity == null || entity.getTeam() == null)
			return 0;
		PlayerStats playerStats = getByMatchIdAndPlayerId(entity.getPlayer().getPlayerId(),
				entity.getMatch().getMatchId());
		return playerStats != null ? 0 : playerStatsMapper.save(entity);
	}

	@Override
	public int update(PlayerStats entity) {
		return playerStatsMapper.update(entity);
	}

	@Override
	public int deleteById(Long id) {
		return playerStatsMapper.deleteById(id);
	}

	public PlayerStats getByMatchIdAndPlayerId(String matchId, String playerId) {
		return playerStatsMapper.getByMatchIdAndPlayerId(playerId, matchId);
	}

	public int insertMatchStats(List<PlayerStats> homeTeamPlayerStats, List<PlayerStats> awayTeamPlayerStats) {
		if (homeTeamPlayerStats == null || awayTeamPlayerStats == null)
			return -1;
		for (PlayerStats ps : homeTeamPlayerStats) {
			try {
				insert(ps);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (PlayerStats ps : awayTeamPlayerStats) {
			try {
				insert(ps);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return 0;
	}
}
