package com.gameservice.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gameservice.domain.Team;
import com.gameservice.parser.TeamParserBasketballRef;
import com.gameservice.repository.TeamRepository;
import com.gameservice.service.TeamService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
	@Autowired
	private TeamRepository teamRepo;
	private TeamParserBasketballRef teamParser = new TeamParserBasketballRef();

	@Override
	@CacheEvict(cacheNames = "teams", key = "#team.id")
	// @Loggable(detail = true)
	public int save(Team team) {
		return team.getPlayers() != null ? teamRepo.saveWithPlayers(team) : teamRepo.insert(team);
	}

	@Override
	@Transactional(readOnly = true)
	public Team getTeam(Long id) {
		// return teamRepo.getById(id);
		return teamCache.getUnchecked(id);
	}

	final LoadingCache<Long, Team> teamCache = CacheBuilder.newBuilder().initialCapacity(50).maximumSize(1000)
			.expireAfterWrite(3, TimeUnit.SECONDS)
			// testing
			.recordStats().build(new CacheLoader<Long, Team>() {
				@Override
				public Team load(Long id) throws Exception {
					return teamRepo.getById(id);
				}
			});

	@Override
	public Team parseTeam(String teamId) {
		String pageUrl = String.format("https://www.basketball-reference.com/teams/%s.html", teamId);
		return teamParser.returnTeamLive(pageUrl);
	}

	@Override
	public List<Team> parseSeasonTeams(int year) {
		return teamParser.readTeamsFromSeason(year);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Team> getSeasonTeams(int year) {
		return teamRepo.getBySeasonYear(year);
	}

	@Override
	@Transactional(readOnly = true)
	public Team getTeamByTeamId(String teamId) {
		return teamRepo.getByTeamId(teamId);
	}
}
