package com.mozzartbet.gameservice.service;

import org.springframework.stereotype.Service;
import com.mozzartbet.gameservice.domain.Team;
import com.mozzartbet.gameservice.parser.TeamParser;

@Service
public class TeamService {
  TeamParser teamParser;
  public void getTeam(String url, String teamName) {
    Team team = teamParser.returnTeamLive(url, teamName);
  }
}
