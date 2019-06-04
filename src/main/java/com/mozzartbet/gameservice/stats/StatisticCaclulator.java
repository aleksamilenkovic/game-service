package com.mozzartbet.gameservice.stats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mozzartbet.gameservice.domain.ActionType;
import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.MatchEvent;
import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.domain.Quarter;
import com.mozzartbet.gameservice.domain.actiontype.EntersOrLeft;
import com.mozzartbet.gameservice.domain.actiontype.FGAttempt;
import com.mozzartbet.gameservice.domain.actiontype.FreeThrow;
import com.mozzartbet.gameservice.domain.actiontype.OtherType;
import com.mozzartbet.gameservice.domain.actiontype.Rebound;
import com.mozzartbet.gameservice.domain.actiontype.ReboundType;
import com.mozzartbet.gameservice.domain.boxscore.PlayerStats;
import com.mozzartbet.gameservice.util.ConvertHelper;

public class StatisticCaclulator {
  // private Map<String, PlayerStats> playersStats;
  static Table<String, String, Integer> lineScore = HashBasedTable.create();
  static String awayTeam = "", homeTeam = "";
  private static Map<String, PlayerStats> playersStats;


  /////////////// ****************** REFAKTORISANJE KODA KROZ OVE ///////////////
  /////////////// ******************\\\\\\\\\\\\\\\\\\\

  /////////////// ****************** DVE PRIVREMENE METODE ///////////////
  private static void minutesPlayed(List<ActionType[]> minutesPlayed, String quarter) {
    minutesPlayed.forEach(actions -> {
      EntersOrLeft enters = (EntersOrLeft) actions[0], left = (EntersOrLeft) actions[1];
      PlayerStats stats = (PlayerStats) playersStats.get(enters.getPlayerId());
      stats.setTimeEntered(enters.getTime());
      PlayerStats stats2 = (PlayerStats) playersStats.get(left.getPlayerId());
      stats2.setTimeLeft(left.getTime());
      stats2.addMinutes();
      stats2.setTimeEntered(12);
    });
  }

  private static void calculatePlayersPercentage(String home, String away) {
    // Iterator it = playersStats.entrySet().iterator();
    PlayerStats homeTeam = new PlayerStats(home, home), awayTeam = new PlayerStats(away, away);
    for (Map.Entry<String, PlayerStats> entry : playersStats.entrySet()) {
      // while (it.hasNext()) {
      // Map.Entry pair = (Map.Entry) it.next();
      PlayerStats stats = (PlayerStats) entry.getValue();
      stats.summary();

      /// OVAJ IF ELSE KASNIJE PREBACITI U MATCH STATS DA IZRACUNA ZA TIM NA KRAJU DA NE BI ZA
      /// SVAKOG IGRACA PROLAZIO
      if (stats.getTeamid().equals(home))
        homeTeam.teamSummary(stats.getFieldGoals(), stats.getFieldGoalAttempts(),
            stats.getThreePointFG(), stats.getThreePointFGAttempts(), stats.getFreeThrows(),
            stats.getFreeThrowAttempts(), stats.getOffensiveRebounds(),
            stats.getDefensiveRebounds(), stats.getAssists(), stats.getSteals(), stats.getBlocks(),
            stats.getTurnovers(), stats.getPersonalFouls());
      else if (stats.getTeamid().equals(away))
        awayTeam.teamSummary(stats.getFieldGoals(), stats.getFieldGoalAttempts(),
            stats.getThreePointFG(), stats.getThreePointFGAttempts(), stats.getFreeThrows(),
            stats.getFreeThrowAttempts(), stats.getOffensiveRebounds(),
            stats.getDefensiveRebounds(), stats.getAssists(), stats.getSteals(), stats.getBlocks(),
            stats.getTurnovers(), stats.getPersonalFouls());
      // it.remove(); // avoids a ConcurrentModificationException
    }
    homeTeam.summary();
    awayTeam.summary();
    playersStats.put(home, homeTeam);
    playersStats.put(away, awayTeam);
  }


  private static void calculatePlayerStats(List<ActionType[]> shots, List<ActionType[]> rebounds,
      List<ActionType[]> freeThrows, List<ActionType[]> turnOversSteals, List<ActionType[]> fouls) {
    shots.forEach(actions -> {
      FGAttempt shootAttempt = (FGAttempt) actions[0];
      PlayerStats stats = (PlayerStats) playersStats.get(shootAttempt.getPlayerId());
      // if (stats != null) {
      stats.addFG(shootAttempt.isMiss(), shootAttempt.isThreePointShoot());
      boolean t = actions.length == 2
          ? playersStats.get(actions[1].getPlayerId()).addAssistOrBlock(shootAttempt.isMiss())
          : false;
      // }
    });
    rebounds.forEach(actions -> {
      Rebound reb = (Rebound) actions[0];
      PlayerStats stats = (PlayerStats) playersStats.get(reb.getPlayerId());
      // if (stats != null)
      stats.addReb(reb.getRebType());

    });

    freeThrows.forEach(actions -> {
      FreeThrow ft = (FreeThrow) actions[0];
      PlayerStats stats = (PlayerStats) playersStats.get(ft.getPlayerId());
      // if (stats != null)
      stats.addFT(ft.isMiss());
    });
    turnOversSteals.forEach(actions -> {
      PlayerStats stats = (PlayerStats) playersStats.get(actions[0].getPlayerId());
      if (stats != null) {
        stats.addTurnovers();
        boolean t =
            actions.length == 2 ? playersStats.get(actions[1].getPlayerId()).addSteals() : false;
      }
    });
    fouls.forEach(actions -> {
      PlayerStats stats = (PlayerStats) playersStats.get(actions[0].getPlayerId());
      // if (stats != null)
      stats.addFoul();
    });
  }

  private static void returnPlayersStats(List<MatchEvent> matchEvents, String quarter) {

    List<ActionType[]> shots = matchEvents.stream()
        .filter(x -> (x.getActions() != null) && (x.getActions()[0] instanceof FGAttempt))
        .map(x -> x.getActions()).collect(Collectors.toList());
    List<ActionType[]> rebounds = matchEvents.stream()
        .filter(x -> x.getActions() != null && x.getActions()[0] instanceof Rebound)
        .map(x -> x.getActions()).collect(Collectors.toList());
    List<ActionType[]> freeThrows = matchEvents.stream()
        .filter(x -> x.getActions() != null && x.getActions()[0] instanceof FreeThrow)
        .map(x -> x.getActions()).collect(Collectors.toList());
    List<ActionType[]> turnoversSteals = matchEvents.stream()
        .filter(x -> x.getActions() != null && x.getActions()[0].getType() == OtherType.TURNOVER)
        .map(x -> x.getActions()).collect(Collectors.toList());
    List<ActionType[]> fouls = matchEvents.stream()
        .filter(x -> x.getActions() != null && x.getActions()[0].getType() == OtherType.FOUL)
        .map(x -> x.getActions()).collect(Collectors.toList());
    List<ActionType[]> minutesPlayed = matchEvents.stream()
        .filter(x -> x.getActions() != null && x.getActions()[0] instanceof EntersOrLeft)
        .map(x -> x.getActions()).collect(Collectors.toList());
    calculatePlayerStats(shots, rebounds, freeThrows, turnoversSteals, fouls);
    minutesPlayed(minutesPlayed, quarter);
  }


  public static Map<String, PlayerStats> returnPlayersStatsIndividual(Match match,
      List<String> homePlayersId, List<String> awayPlayersId) {
    if (match.getQuarters() == null || awayPlayersId == null || homePlayersId == null)
      return null;
    playersStats = new HashMap<String, PlayerStats>();

    for (String playerId : homePlayersId)
      playersStats.put(playerId, new PlayerStats(playerId, match.getHomeTeam()));
    for (String playerId : awayPlayersId)
      playersStats.put(playerId, new PlayerStats(playerId, match.getAwayTeam()));
    for (Quarter q : match.getQuarters())
      returnPlayersStats(q.getMatchEvents(), q.getQuarterName());// metoda puni mapu sa statistikama
                                                                 // igraca

    calculatePlayersPercentage(match.getHomeTeam(), match.getAwayTeam());
    return playersStats;
  }


  /////////////// ***************************************************************************************************************************\\\\\\\\\\\\\\\\\\\\\\\\
  /////////////// ***************************************************************************************************************************\\\\\\\\\\\\\\\\\\\\\\\\
  /////////////// ***************************************************************************************************************************\\\\\\\\\\\\\\\\\\\\\\\\
  /////////////// ***************************************************************************************************************************\\\\\\\\\\\\\\\\\\\\\\\\
  // ISPOD SU STARE FUNKCIJE KOJE NISU PREKO STRIMOVA I LAMBDA FUNKCIJA vec if else//
  public static PlayerStats calculatePlayerStats(List<MatchEvent> matchEvents, String playerId,
      boolean quarterPointsEmpty) {

    PlayerStats playerStats;
    int fieldGoals = 0, fieldGoalAttempts = 0, threePointFG = 0, threePointFGAttempts = 0,
        freeThrows = 0, freeThrowAttempts = 0, offensiveRebounds = 0, defensiveRebounds = 0,
        totalRebounds = 0, assists = 0, steals = 0, blocks = 0, turnovers = 0, personalFouls = 0,
        points = 0, plusMinusWhenEntered = 0, plusMinus = 0, i = 0; // i je za
    float minutesWhenEntered = 0, minutesPlayed = 0, minutesWhenLeft = 12; // actions[i]
    // prvi ili
    // drugi
    // igrac u akciji
    double fieldGoalPercentage = 0, threePointFGPercentage = 0, freeThrowPercentage = 0;
    for (MatchEvent event : matchEvents) {
      if (!event.getNeutralAction().isEmpty()) {
        if (event.getNeutralAction().contains("End"))
          if (minutesWhenLeft > minutesWhenEntered) {// ako igrac nije izasao znaci da je igrao celu
            minutesPlayed = minutesWhenEntered;// cetvrtinu ili odkad je usao
            minutesWhenEntered = 12;
          }
        continue;
      }
      if (event.getActions() == null || event.getActions().length == 0)
        continue;
      ActionType actions[] = event.getActions();

      if (quarterPointsEmpty)
        sumQuartersByTeam(event);

      if (actions[0].getPlayerId().equals(playerId))
        i = 0;
      else if (actions.length == 2 && actions[1] != null
          && actions[1].getPlayerId().equals(playerId))
        i = 1;
      else
        continue;

      if (actions[i].getPlayerId().equals(playerId)) {
        if (actions[i] instanceof FGAttempt) {
          FGAttempt fg = (FGAttempt) actions[i];
          fieldGoalAttempts++;
          if (fg.isThreePointShoot()) {
            threePointFGAttempts++;
            if (!fg.isMiss()) {
              threePointFG++;
              fieldGoals++;
            }
          } else if (!fg.isMiss())
            fieldGoals++;
        } else if (actions[i] instanceof FreeThrow) {
          FreeThrow ft = (FreeThrow) actions[i];
          freeThrowAttempts++;
          if (!ft.isMiss())
            freeThrows++;
        } else if (actions[i] instanceof Rebound) {
          Rebound reb = (Rebound) actions[i];
          if (reb.getRebType() == ReboundType.DEFENSIVE)
            defensiveRebounds++;
          else
            offensiveRebounds++;
        } else if (actions[i].getType() == OtherType.TURNOVER) {
          turnovers++;
        } else if (actions[i].getType() == OtherType.FOUL) {
          personalFouls++;
        } else if (actions[i].getType() == OtherType.ASSIST) {
          assists++;
        } else if (actions[i].getType() == OtherType.BLOCK) {
          blocks++;
        } else if (actions[i].getType() == OtherType.STEAL) {
          steals++;
          // } else if (actions[i].getType() == OtherType.ENTERSTHECOURT) {
          plusMinusWhenEntered =
              event.getAwayTeamAction().length() > 2 ? event.getResultHomeLead() * (-1)
                  : event.getResultHomeLead();

          // } else if (actions[i].getType() == OtherType.LEAVESTHECOURT) {

          /*
           * if (minutesPlayed == 0) minutesPlayed = event.getQuarter().contains("OT") ? (float)
           * event.getQuarter().charAt(0) * 5 : (float) event.getQuarter().charAt(0) * 12; else min
           * 
           * plusMinus += event.getAwayTeamAction().length() > 2 ? event.getResultHomeLead() * (-1)
           * : event.getResultHomeLead() - plusMinusWhenEntered; plusMinusWhenEntered = -1000;
           */
        }
      }

    }
    if (plusMinusWhenEntered != -1000) {
      MatchEvent event = matchEvents.get(matchEvents.size() - 2);
      plusMinus += event.getAwayTeamAction().length() > 2 ? event.getResultHomeLead() * (-1)
          : event.getResultHomeLead() - plusMinusWhenEntered;
    }
    totalRebounds = offensiveRebounds + defensiveRebounds;
    if (freeThrows == 0)
      freeThrowPercentage = 0;
    else
      freeThrowPercentage =
          ConvertHelper.roundDecimal(((double) freeThrows / freeThrowAttempts), 3);
    if (threePointFG == 0)
      threePointFGPercentage = 0;
    else
      threePointFGPercentage =
          ConvertHelper.roundDecimal(((double) threePointFG / threePointFGAttempts), 3);
    if (fieldGoals == 0)
      fieldGoalPercentage = 0;
    else
      fieldGoalPercentage =
          ConvertHelper.roundDecimal(((double) fieldGoals / fieldGoalAttempts), 3);

    points = freeThrows + 2 * (fieldGoals - threePointFG) + 3 * threePointFG;

    playerStats = new PlayerStats(playerId, fieldGoals, fieldGoalAttempts, fieldGoalPercentage,
        threePointFG, threePointFGAttempts, threePointFGPercentage, freeThrows, freeThrowAttempts,
        freeThrowPercentage, offensiveRebounds, defensiveRebounds, totalRebounds, assists, steals,
        blocks, turnovers, personalFouls, points, plusMinus, minutesPlayed, 0, 0, "");
    return playerStats;
  }

  public static List<PlayerStats> returnTeamStatsIndividual(List<MatchEvent> matchEvents,
      List<Player> players) {

    List<PlayerStats> playerStats = new ArrayList<PlayerStats>();
    if (matchEvents != null && players != null) {
      for (Player p : players) {
        boolean quarterPointsEmpty = lineScore.isEmpty();
        PlayerStats ps = calculatePlayerStats(matchEvents, p.getPLAYER_ID(), quarterPointsEmpty);
        System.out.println(ps);
        playerStats.add(ps);
      }
    } else
      return null;

    return playerStats;

  }

  public static void sumQuartersByTeam(MatchEvent event) {
    int pointsTeamMade;
    if (event.getPointsMadeAwayTeam() <= 0) {
      if (event.getPointsMadeHomeTeam() > 0) {
        pointsTeamMade = event.getPointsMadeHomeTeam();
        lineScore.put(event.getQuarter(), homeTeam,
            (lineScore.get(event.getQuarter(), homeTeam) != null
                ? lineScore.get(event.getQuarter(), homeTeam) + pointsTeamMade
                : 0 + pointsTeamMade));
      }
    } else {
      pointsTeamMade = event.getPointsMadeAwayTeam();
      lineScore.put(event.getQuarter(), awayTeam,
          (lineScore.get(event.getQuarter(), awayTeam) != null
              ? lineScore.get(event.getQuarter(), awayTeam) + pointsTeamMade
              : 0 + pointsTeamMade));
    }
  }

}
