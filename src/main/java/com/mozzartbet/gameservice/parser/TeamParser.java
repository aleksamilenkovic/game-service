package com.mozzartbet.gameservice.parser;

import java.util.List;
import com.google.common.collect.Multimap;
import com.mozzartbet.gameservice.domain.Team;

public interface TeamParser {
  public Team returnTeamLive(String pageUrl);

  public Team returnTeamLocal(String fileName);

  public List<Team> readTeamsFromSeason(int seasonYear);

  public Multimap<Integer, List<Team>> readTeamsFromSpecificSeasonTillNow(int year);
}
