package com.mozzartbet.gameservice.stats;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.Quarter;
import com.mozzartbet.gameservice.domain.boxscore.PlayerStats;
import lombok.Data;

@Data
public class MatchStats {
  private String matchId;
  private String awayTeamName;
  private String homeTeamName;
  private Table<String, String, Integer> lineScore = HashBasedTable.create(); // zbir poena po
                                                                              // cetvrtinama i
                                                                              // timu
  private TreeMap<String, Integer> bestPlayersStats; // OVDE JE MAPA 10 NAJBOLJIH IGRACA NA UTAKMICI
                                                     // PO KRITERIJUMU(POENI,ASISTENCIJE ili
                                                     // SKOKOVI)
  private Map<String, PlayerStats> playersStats;// statistika za svakog igraca posebno na mecu iz

  private List<PlayerStats> homeTeamPlayerStats;
  private List<PlayerStats> awayTeamPlayerStats;

  private PlayerStats awayTeamStatsSummary; // statistika po timu TOTAL
  private PlayerStats homeTeamStatsSummary;
  // u teamStatsSummary se smesta summary stats tima jer je model isti samo za playerId stavljam
  // teamName
  private Match match;

  public MatchStats() {
    homeTeamPlayerStats = new ArrayList<PlayerStats>();
    awayTeamPlayerStats = new ArrayList<PlayerStats>();
  }



  public void calculateMatchStats(Match match, String sortType) {
    this.match = match;
    createLineScore(); // pravi linescore poena po cetvrtinama i timu
    playersStats = StatisticCaclulator.returnPlayersStatsIndividual(match, match.getHomePlayersID(),
        match.getAwayPlayersID());
    summaryStats();

    // najbolji igraci po sortType-u
    statsByTeam();
    sort(sortType);
  }

  private void createLineScore() {
    int homeTeamPoints = 0, awayTeamPoints = 0;
    for (Quarter q : match.getQuarters()) {
      lineScore.put(match.getAwayTeam(), q.getQuarterName(),
          q.getPointsAwayTeam() - awayTeamPoints);
      lineScore.put(match.getHomeTeam(), q.getQuarterName(),
          q.getPointsHomeTeam() - homeTeamPoints);
      homeTeamPoints = q.getPointsHomeTeam();
      awayTeamPoints = q.getPointsAwayTeam();
    }
    lineScore.put(match.getAwayTeam(), "TOTAL", match.getAwayTeamPoints());
    lineScore.put(match.getHomeTeam(), "TOTAL", match.getHomeTeamPoints());
  }

  private void statsByTeam() {
    for (String playerId : match.getHomePlayersID())
      homeTeamPlayerStats.add(playersStats.get(playerId));
    for (String playerId : match.getAwayPlayersID())
      awayTeamPlayerStats.add(playersStats.get(playerId));
  }



  public void sort(String sortType) {
    if (sortType.equals("assist"))
      sortByAssists();
    else if (sortType.equals("rebound"))
      sortByRebounds();
    else
      sortByPoints();
  }

  public void sortByPoints() {
    System.out.println("BEST PLAYERS BY POINTS: ////");
    awayTeamPlayerStats.sort((Comparator.comparing(PlayerStats::getPoints)).reversed());
    homeTeamPlayerStats.sort((Comparator.comparing(PlayerStats::getPoints)).reversed());
    HashMap<String, Integer> map = new HashMap<String, Integer>();
    for (int i = 0; i < 5; i++) {
      map.put(awayTeamPlayerStats.get(i).getPlayerId(), awayTeamPlayerStats.get(i).getPoints());
      map.put(homeTeamPlayerStats.get(i).getPlayerId(), homeTeamPlayerStats.get(i).getPoints());
    }
    sortBestPlayers(map);
  }

  public void sortByAssists() {
    System.out.println("BEST PLAYERS BY ASSISTS : ////");
    awayTeamPlayerStats.sort((Comparator.comparing(PlayerStats::getAssists)).reversed());
    homeTeamPlayerStats.sort((Comparator.comparing(PlayerStats::getAssists)).reversed());
    HashMap<String, Integer> map = new HashMap<String, Integer>();
    for (int i = 0; i < 5; i++) {
      map.put(awayTeamPlayerStats.get(i).getPlayerId(), awayTeamPlayerStats.get(i).getAssists());
      map.put(homeTeamPlayerStats.get(i).getPlayerId(), homeTeamPlayerStats.get(i).getAssists());
    }
    sortBestPlayers(map);
  }

  public void sortByRebounds() {
    System.out.println("BEST PLAYERS BY REBOUNDS : ////");
    awayTeamPlayerStats.sort((Comparator.comparing(PlayerStats::getTotalRebounds)).reversed());
    homeTeamPlayerStats.sort((Comparator.comparing(PlayerStats::getTotalRebounds)).reversed());
    HashMap<String, Integer> map = new HashMap<String, Integer>();
    for (int i = 0; i < 5; i++) {
      map.put(awayTeamPlayerStats.get(i).getPlayerId(),
          awayTeamPlayerStats.get(i).getTotalRebounds());
      map.put(homeTeamPlayerStats.get(i).getPlayerId(),
          homeTeamPlayerStats.get(i).getTotalRebounds());
    }
    sortBestPlayers(map);
  }

  public void sortBestPlayers(HashMap<String, Integer> map) {
    ValueComparator bvc = new ValueComparator(map);
    bestPlayersStats = new TreeMap<String, Integer>(bvc);
    bestPlayersStats.putAll(map);
  }

  private void summaryStats() {
    awayTeamStatsSummary = playersStats.get(match.getAwayTeam());
    playersStats.remove(match.getAwayTeam());
    homeTeamStatsSummary = playersStats.get(match.getHomeTeam());
    playersStats.remove(match.getHomeTeam());
  }

}
