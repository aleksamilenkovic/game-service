package com.gameservice.service;

import com.gameservice.domain.Season;

public interface SeasonService {

	public int saveSeason(Season season);

	public Season parseSeason(int year);

	public Season getSeason(int year);

	public Season parseAndSaveAll(int year);

	public Season getWithAllTeamsAndMatches(int year);
}
