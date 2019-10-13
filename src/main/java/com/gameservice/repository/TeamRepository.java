package com.gameservice.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gameservice.domain.Player;
import com.gameservice.domain.Team;
import com.gameservice.mapper.TeamMapper;

@Repository
public class TeamRepository implements BaseRepository<Team> {
	@Autowired
	private TeamMapper teamMapper;
	@Autowired
	private PlayerRepository playerRepo;

	@Override
	public long count() {
		return teamMapper.count();
	}

	@Override
	public Team getById(Long id) {
		return teamMapper.getById(id);
	}

	public Team getByTeamId(String teamId) {
		return teamMapper.getWithPlayersByTeamId(teamId);
	}

	@Override
	public int insert(Team entity) {
		if (entity == null)
			return 0;
		Team player = getByTeamId(entity.getTeamId());
		return player != null ? 0 : teamMapper.save(entity);
	}

	@Override
	public int update(Team entity) {
		return teamMapper.update(entity);
	}

	@Override
	public int deleteById(Long id) {
		return teamMapper.deleteById(id);
	}

	public int saveWithPlayers(Team team) {
		insert(team);
		for (Player p : team.getPlayers())
			playerRepo.insert(p);
		return 1;
	}

	public List<Team> getBySeasonYear(int year) {
		return playerRepo.getSeasonTeamsAndPlayers(year);
	}

}
