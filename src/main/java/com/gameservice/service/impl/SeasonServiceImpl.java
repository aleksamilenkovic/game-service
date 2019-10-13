package com.gameservice.service.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gameservice.domain.Season;
import com.gameservice.repository.SeasonRepository;
import com.gameservice.service.MatchService;
import com.gameservice.service.SeasonService;
import com.gameservice.service.TeamService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeasonServiceImpl implements SeasonService {
	@Autowired
	private TeamService teamService;
	@Autowired
	private SeasonRepository seasonRepo;
	@Autowired
	private MatchService matchService;

	@Override
	public int saveSeason(Season season) {
		return seasonRepo.insert(season);
	}

	@Override
	public Season parseSeason(int year) {
		Season season = Season.builder().seasonYear(year).build();
		season.setTeams(teamService.parseSeasonTeams(year));
		season.setSeasonMatches(matchService.parseSeasonMatches(year));
		return season;
	}

	@Transactional(readOnly = true)
	@Override
	public Season getSeason(int year) {
		// return seasonRepo.getByYear(year);
		return seasonCache.getUnchecked(year);
	}

	final LoadingCache<Integer, Season> seasonCache = CacheBuilder.newBuilder().initialCapacity(5).maximumSize(100)
			.expireAfterWrite(3, TimeUnit.HOURS)
			// testing
			.recordStats().build(new CacheLoader<Integer, Season>() {
				@Override
				public Season load(Integer year) throws Exception {
					return getWithAllTeamsAndMatches(year);
				}
			});

	@Override
	public Season parseAndSaveAll(int year) {
		Season season = parseSeason(year);
		saveSeason(season);
		season.getTeams().forEach(team -> teamService.save(team));
		season.getSeasonMatches().forEach(match -> matchService.save(match));
		return season;
	}

	@Transactional(readOnly = true)
	@Override
	public Season getWithAllTeamsAndMatches(int year) {
		Season season = getSeason(year);
		season.setTeams(teamService.getSeasonTeams(year));
		season.setSeasonMatches(matchService.getSeasonMatches(year));
		return season;
	}
}
