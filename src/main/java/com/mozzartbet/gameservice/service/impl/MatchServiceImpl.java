package com.mozzartbet.gameservice.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.parser.MatchParserBasketballRef;
import com.mozzartbet.gameservice.repository.MatchEventRepository;
import com.mozzartbet.gameservice.repository.MatchRepository;
import com.mozzartbet.gameservice.repository.QuarterRepository;
import com.mozzartbet.gameservice.service.MatchService;

public class MatchServiceImpl implements MatchService {
  @Autowired
  private MatchRepository matchRepo;
  @Autowired
  private QuarterRepository quarterRepo;
  @Autowired
  private MatchEventRepository eventRepo;

  private MatchParserBasketballRef matchParser = new MatchParserBasketballRef();

  @Override
  public int save(Match match) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public Match getMatch(String matchId) {
    return matchRepo.getByMatchId(matchId);
  }

  @Override
  public Match parseMatch(String matchId) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<Match> parseSeasonMatches(int year) {
    // TODO Auto-generated method stub
    return null;
  }

}
