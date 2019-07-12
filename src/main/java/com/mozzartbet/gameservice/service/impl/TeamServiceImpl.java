package com.mozzartbet.gameservice.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mozzartbet.gameservice.domain.Team;
import com.mozzartbet.gameservice.parser.TeamParserBasketballRef;
import com.mozzartbet.gameservice.repository.TeamRepository;
import com.mozzartbet.gameservice.service.TeamService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
  @Autowired
  private TeamRepository teamRepo;
  private TeamParserBasketballRef teamParser = new TeamParserBasketballRef();

  @Override
  public int save(Team team) {
    return team.getPlayers() != null ? teamRepo.saveWithPlayers(team) : teamRepo.insert(team);
  }

  @Override
  public Team getTeam(Long id) {
    return teamRepo.getById(id);
  }

  @Override
  public Team parseTeam(String teamId) {
    String pageUrl = String.format("https://www.basketball-reference.com/teams/%s.html", teamId);
    return teamParser.returnTeamLive(pageUrl);
  }

  @Override
  public List<Team> parseSeasonTeams(int year) {
    return teamParser.readTeamsFromSeason(year);
  }

}
