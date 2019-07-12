package com.mozzartbet.gameservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mozzartbet.gameservice.domain.Season;
import com.mozzartbet.gameservice.repository.SeasonRepository;
import com.mozzartbet.gameservice.service.SeasonService;
import com.mozzartbet.gameservice.service.TeamService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeasonServiceImpl implements SeasonService {
  @Autowired
  private TeamService teamService;
  @Autowired
  private SeasonRepository seasonRepo;

  @Override
  public int saveSeason(Season season) {
    return seasonRepo.insert(season);
  }

  @Override
  public Season parseSeason(int year) {
    Season season = Season.builder().seasonYear(year).build();
    season.setTeams(teamService.parseSeasonTeams(year));
    return season;
  }

  @Override
  public Season getSeason(int year) {
    return seasonRepo.getByYear(year);
  }

  @Override
  public Season parseAndSaveAll(int year) {
    Season season = parseSeason(year);
    saveSeason(season);
    season.getTeams().forEach(team -> teamService.save(team));
    // and matches here too
    return season;
  }
}
