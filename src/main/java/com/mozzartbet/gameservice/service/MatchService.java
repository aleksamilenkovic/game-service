package com.mozzartbet.gameservice.service;

import java.util.List;
import com.mozzartbet.gameservice.domain.Match;

public interface MatchService {

  public int save(Match match);

  public Match getMatch(String matchId);

  public Match parseMatch(String matchId);

  public List<Match> parseSeasonMatches(int year);
}
