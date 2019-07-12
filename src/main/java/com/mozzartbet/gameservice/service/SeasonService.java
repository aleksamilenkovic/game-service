package com.mozzartbet.gameservice.service;

import com.mozzartbet.gameservice.domain.Season;

public interface SeasonService {

  public int saveSeason(Season season);

  public Season parseSeason(int year);

  public Season getSeason(int year);

  public Season parseAndSaveAll(int year);
}
