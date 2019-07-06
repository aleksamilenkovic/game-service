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
import com.mozzartbet.gameservice.util.ValueComparator;
import lombok.Data;

@Data
public class MatchStatsCalculator {
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

  private List<PlayerStats> homeTeamPlayerStats = new ArrayList<PlayerStats>();
  private List<PlayerStats> awayTeamPlayerStats = new ArrayList<PlayerStats>();

  private PlayerStats awayTeamStatsSummary; // statistika po timu TOTAL
  private PlayerStats homeTeamStatsSummary;
  // u teamStatsSummary se smesta summary stats tima jer je model isti samo za playerId stavljam
  // teamName
  private Match match;



  public void calculateMatchStats(Match match, String sortType) {
    this.match = match;
    createLineScore();
    // pravi linescore poena po cetvrtinama i timu
    StatisticCaclulator scalc = new StatisticCaclulator();
    playersStats = scalc.returnPlayersStatsIndividual(match, match.getHomePlayersID(),
        match.getAwayPlayersID());
    summaryStats();

    // najbolji igraci po sortType-u
    statsByTeam();
    // showMatchStats();
    sort(sortType);
    // showBestPlayers();
  }

  private void createLineScore() {
    int homeTeamPoints = 0, awayTeamPoints = 0;
    for (Quarter q : match.getQuarters()) {
      lineScore.put(match.getAwayTeam().getName(), q.getName(),
          q.getPointsAwayTeam() - awayTeamPoints);
      lineScore.put(match.getHomeTeam().getName(), q.getName(),
          q.getPointsHomeTeam() - homeTeamPoints);
      homeTeamPoints = q.getPointsHomeTeam();
      awayTeamPoints = q.getPointsAwayTeam();
    }
    lineScore.put(match.getAwayTeam().getName(), "TOTAL", match.getAwayTeamPoints());
    lineScore.put(match.getHomeTeam().getName(), "TOTAL", match.getHomeTeamPoints());
  }

  private void statsByTeam() {
    for (String playerId : match.getHomePlayersID())
      homeTeamPlayerStats.add(playersStats.get(playerId));
    for (String playerId : match.getAwayPlayersID())
      awayTeamPlayerStats.add(playersStats.get(playerId));
  }



  public void sort(String sortType) {
    if (sortType == null)
      return;
    if (sortType.equals("assist"))
      sortByAssists();
    else if (sortType.equals("rebound"))
      sortByRebounds();
    else if (sortType.equals("points"))
      sortByPoints();
  }

  public void sortByPoints() {
    awayTeamPlayerStats.sort((Comparator.comparing(PlayerStats::getPoints)).reversed());
    homeTeamPlayerStats.sort((Comparator.comparing(PlayerStats::getPoints)).reversed());
    HashMap<String, Integer> map = new HashMap<String, Integer>();
    for (int i = 0; i < 5; i++) {
      map.put(awayTeamPlayerStats.get(i).getPlayer().getPlayerId(),
          awayTeamPlayerStats.get(i).getPoints());
      map.put(homeTeamPlayerStats.get(i).getPlayer().getPlayerId(),
          homeTeamPlayerStats.get(i).getPoints());
    }
    sortBestPlayers(map);
  }

  public void sortByAssists() {
    awayTeamPlayerStats.sort((Comparator.comparing(PlayerStats::getAssists)).reversed());
    homeTeamPlayerStats.sort((Comparator.comparing(PlayerStats::getAssists)).reversed());
    HashMap<String, Integer> map = new HashMap<String, Integer>();
    for (int i = 0; i < 5; i++) {
      map.put(awayTeamPlayerStats.get(i).getPlayer().getPlayerId(),
          awayTeamPlayerStats.get(i).getAssists());
      map.put(homeTeamPlayerStats.get(i).getPlayer().getPlayerId(),
          homeTeamPlayerStats.get(i).getAssists());
    }
    sortBestPlayers(map);
  }

  public void sortByRebounds() {
    awayTeamPlayerStats.sort((Comparator.comparing(PlayerStats::getTotalRebounds)).reversed());
    homeTeamPlayerStats.sort((Comparator.comparing(PlayerStats::getTotalRebounds)).reversed());
    HashMap<String, Integer> map = new HashMap<String, Integer>();
    for (int i = 0; i < 5; i++) {
      map.put(awayTeamPlayerStats.get(i).getPlayer().getPlayerId(),
          awayTeamPlayerStats.get(i).getTotalRebounds());
      map.put(homeTeamPlayerStats.get(i).getPlayer().getPlayerId(),
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
    awayTeamStatsSummary = playersStats.get(match.getAwayTeam().getTeamId());
    playersStats.remove(match.getAwayTeam().getTeamId());
    homeTeamStatsSummary = playersStats.get(match.getHomeTeam().getTeamId());
    playersStats.remove(match.getHomeTeam().getTeamId());
  }


  public void showMatchStats() {
    showLineScore();
    showBoxScore();
  }

  public void showLineScore() {
    Map<String, Integer> awayteamPointsByQuarters = lineScore.row(match.getAwayTeam().getName());
    Map<String, Integer> homeTeamPointsByQuarters = lineScore.row(match.getHomeTeam().getName());
    System.out.println("________________________________________________________________");
    System.out.print("\t\t\t");
    for (Quarter quarter : match.getQuarters())
      System.out.print("| " + quarter.getName());
    System.out.print("| TOTAL");
    System.out.print("\n" + match.getAwayTeam() + ":");
    for (Map.Entry<String, Integer> entry : awayteamPointsByQuarters.entrySet())
      System.out.print("  |  " + entry.getValue());
    System.out.print("\n" + match.getHomeTeam() + ":      ");
    for (Map.Entry<String, Integer> entry : homeTeamPointsByQuarters.entrySet())
      System.out.print("  |  " + entry.getValue());
    System.out.println("\n________________________________________________________________\n");
  }

  public void showBoxScore() {
    System.out.println("\n\n" + match.getAwayTeam() + "======================== Basic box score\n");
    System.out.println(
        "Players       | FG | FGA | FG% | 3P | 3PA | 3P% | FT | FTA | FT% | ORB | DRB TRB AST STL BLK TOV PF  PTS +/-");
    for (PlayerStats ps : awayTeamPlayerStats)
      System.out.println(ps);
    System.out.println("\n\n" + match.getHomeTeam() + "======================== Basic box score\n");
    for (PlayerStats ps : homeTeamPlayerStats)
      System.out.println(ps);
  }



  public void showBestPlayers() {
    int i = 0;
    for (Map.Entry<String, Integer> entry : bestPlayersStats.entrySet()) {
      System.out.print(entry.getKey() + "  :  " + (int) entry.getValue() + " | ");
      if (i++ == 4)// najboljih pet po sortType-u
        break;
    }
    System.out.println("\n________________________________________________________________\n");
  }
}
