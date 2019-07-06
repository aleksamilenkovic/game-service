package com.mozzartbet.gameservice.stats;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.MatchEvent;
import com.mozzartbet.gameservice.domain.MatchEventType;
import com.mozzartbet.gameservice.domain.Quarter;
import com.mozzartbet.gameservice.domain.boxscore.PlayerStats;

public class StatisticCaclulator {
  // private Map<String, PlayerStats> playersStats;
  static Table<String, String, Integer> lineScore = HashBasedTable.create();
  static String awayTeam = "", homeTeam = "";
  private Map<String, PlayerStats> playersStats;


  /*
   * /////////////// ****************** /////////////// private static void
   * minutesPlayed(List<ActionType[]> minutesPlayed, String quarter) { minutesPlayed.forEach(actions
   * -> { EntersOrLeft enters = (EntersOrLeft) actions[0], left = (EntersOrLeft) actions[1];
   * PlayerStats stats = (PlayerStats) playersStats.get(enters.getPlayerId());
   * stats.setTimeEntered(enters.getTime()); PlayerStats stats2 = (PlayerStats)
   * playersStats.get(left.getPlayerId()); stats2.setTimeLeft(left.getTime()); stats2.addMinutes();
   * stats2.setTimeEntered(12); }); }
   */

  // metoda koja na kraju izracuna za svakog igraca procente za statistiku
  // i za tim posebno
  private void calculatePlayersPercentage(Match match) {
    // Iterator it = playersStats.entrySet().iterator();
    PlayerStats homeTeam = new PlayerStats(null, match.getHomeTeam(), match),
        awayTeam = new PlayerStats(null, match.getAwayTeam(), match);
    for (Map.Entry<String, PlayerStats> entry : playersStats.entrySet()) {
      // while (it.hasNext()) {
      // Map.Entry pair = (Map.Entry) it.next();
      PlayerStats stats = (PlayerStats) entry.getValue();
      stats.summary();
      PlayerStats team =
          stats.getTeam().getTeamId().equals(match.getHomeTeam().getTeamId()) ? homeTeam : awayTeam;
      team.teamSummary(stats);
    }
    homeTeam.summary();
    awayTeam.summary();
    playersStats.put(match.getHomeTeam().getTeamId(), homeTeam);
    playersStats.put(match.getAwayTeam().getTeamId(), awayTeam);
  }

  private void returnPlayersStats(List<MatchEvent> matchEvents, String quarter) {
    matchEvents.stream().filter(x -> x.getEventType() == MatchEventType.SHOOTFOR2).forEach(x -> {
      playersStats.get(x.getFirstPlayer().getPlayerId()).add2point(x.getPointsMade() != 2);
      if (x.getSecondPlayer() != null)
        playersStats.get(x.getSecondPlayer().getPlayerId())
            .addAssistOrBlock(x.getPointsMade() == 2);
    });;
    matchEvents.stream().filter(x -> x.getEventType() == MatchEventType.SHOOTFOR3).forEach(x -> {
      playersStats.get(x.getFirstPlayer().getPlayerId()).add3point(x.getPointsMade() != 3);
      if (x.getSecondPlayer() != null)
        playersStats.get(x.getSecondPlayer().getPlayerId())
            .addAssistOrBlock(x.getPointsMade() != 3);
    });;
    matchEvents.stream().filter(x -> x.getEventType() == MatchEventType.FREETHROW).forEach(x -> {
      playersStats.get(x.getFirstPlayer().getPlayerId()).addFreeThrow(x.getPointsMade() != 1);
    });;
    matchEvents.stream().filter(x -> x.getEventType() == MatchEventType.REBOUND).forEach(x -> {
      playersStats.get(x.getFirstPlayer().getPlayerId()).addRebound(x.offensiveRebound());
    });;
    matchEvents.stream().filter(x -> x.getEventType() == MatchEventType.TURNOVER).forEach(x -> {
      playersStats.get(x.getFirstPlayer().getPlayerId()).addTurnovers();
      if (x.getSecondPlayer() != null)
        playersStats.get(x.getSecondPlayer().getPlayerId()).addSteals();
    });;
    matchEvents.stream().filter(x -> x.getEventType() == MatchEventType.FOUL).forEach(x -> {
      playersStats.get(x.getFirstPlayer().getPlayerId()).addFoul();
    });;
  }


  public Map<String, PlayerStats> returnPlayersStatsIndividual(Match match,
      List<String> homePlayersId, List<String> awayPlayersId) {
    if (match.getQuarters() == null || awayPlayersId == null || homePlayersId == null)
      return null;
    playersStats = new HashMap<String, PlayerStats>();

    for (String playerId : homePlayersId)
      playersStats.put(playerId, new PlayerStats(playerId, match.getHomeTeam(), match));
    for (String playerId : awayPlayersId)
      playersStats.put(playerId, new PlayerStats(playerId, match.getAwayTeam(), match));
    for (Quarter q : match.getQuarters())
      returnPlayersStats(q.getMatchEvents(), q.getName());// metoda puni mapu sa statistikama
                                                          // igraca

    calculatePlayersPercentage(match);
    return playersStats;
  }


}
