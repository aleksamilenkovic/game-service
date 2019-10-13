package com.gameservice.service;

import java.util.List;

import com.gameservice.domain.Match;

public interface MatchService {

	public int save(Match match);

	public Match getMatch(String matchId);

	public Match parseMatch(String matchId);

	public List<Match> parseSeasonMatches(int year);

	public List<Match> getSeasonMatches(int year);
}
