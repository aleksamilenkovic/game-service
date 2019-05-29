package com.mozzartbet.gameservice.stats;

import java.util.LinkedList;
import com.mozzartbet.gameservice.domain.ActionType;
import com.mozzartbet.gameservice.domain.MatchEvent;
import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.domain.actiontype.FGAttempt;
import com.mozzartbet.gameservice.domain.actiontype.FreeThrow;
import com.mozzartbet.gameservice.domain.actiontype.OtherType;
import com.mozzartbet.gameservice.domain.actiontype.Rebound;
import com.mozzartbet.gameservice.domain.actiontype.ReboundType;
import com.mozzartbet.gameservice.domain.boxscore.PlayerStats;
import com.mozzartbet.gameservice.util.ConvertHelper;

public class StatisticCaclulator {
  // private Map<String, PlayerStats> playersStats;

  public static PlayerStats calculatePlayerStats(LinkedList<MatchEvent> matchEvents,
      String playerId) {

    PlayerStats playerStats;
    int fieldGoals = 0, fieldGoalAttempts = 0, threePointFG = 0, threePointFGAttempts = 0,
        freeThrows = 0, freeThrowAttempts = 0, offensiveRebounds = 0, defensiveRebounds = 0,
        totalRebounds = 0, assists = 0, steals = 0, blocks = 0, turnovers = 0, personalFouls = 0,
        points = 0, i = 0; // i je za actions[i] prvi ili drugi igrac u akciji
    double fieldGoalPercentage = 0, threePointFGPercentage = 0, freeThrowPercentage = 0;

    for (MatchEvent event : matchEvents) {
      if (!event.getNeutralAction().isEmpty() || event.getActions() == null
          || event.getActions().length == 0)
        continue;
      ActionType actions[] = event.getActions();

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
        }
      }

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
        blocks, turnovers, personalFouls, points);
    return playerStats;
  }

  public static LinkedList<PlayerStats> returnTeamStatsIndividual(
      LinkedList<MatchEvent> matchEvents, LinkedList<Player> players) {

    LinkedList<PlayerStats> playerStats = new LinkedList<PlayerStats>();
    if (matchEvents != null && players != null) {
      for (Player p : players) {
        PlayerStats ps = calculatePlayerStats(matchEvents, p.getId());
        // System.out.println(ps);
        playerStats.add(ps);
      }
    } else
      return null;

    return playerStats;
  }



}
