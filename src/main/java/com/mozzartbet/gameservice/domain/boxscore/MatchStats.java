package com.mozzartbet.gameservice.domain.boxscore;

import java.util.List;
import com.google.common.collect.Table;
import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.stats.MatchStatsCalculator;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MatchStats {
  private String matchId;
  private Table<String, String, Integer> lineScore;
  private List<PlayerStats> homeTeamPlayerStats;
  private List<PlayerStats> awayTeamPlayerStats;

  public void calculateMatchStats(Match match) {
    MatchStatsCalculator msc = new MatchStatsCalculator();
    msc.calculateMatchStats(match, null);
    matchId = match.getMatchId();
    lineScore = msc.getLineScore();
    homeTeamPlayerStats = msc.getHomeTeamPlayerStats();
    awayTeamPlayerStats = msc.getAwayTeamPlayerStats();
  }
}
