package com.mozzartbet.gameservice.domain.boxscore;

import com.mozzartbet.gameservice.util.ConvertHelper;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PlayerStats {
  public PlayerStats(String playerId, String teamName) {
    this.playerId = playerId;
    this.teamid = teamName;
  }

  private String playerId;
  private int fieldGoals = 0;
  private int fieldGoalAttempts = 0;
  private double fieldGoalPercentage = 0;
  private int threePointFG = 0;
  private int threePointFGAttempts = 0;
  private double threePointFGPercentage = 0;
  private int freeThrows = 0;
  private int freeThrowAttempts = 0;
  private double freeThrowPercentage = 0;
  private int offensiveRebounds = 0;
  private int defensiveRebounds = 0;
  private int totalRebounds = 0;
  private int assists = 0;
  private int steals = 0;
  private int blocks = 0;
  private int turnovers = 0;
  private int personalFouls = 0;
  private int points = 0;
  private int plusMinus = 0;
  private float minutesPlayed = 0;
  private float timeEntered = 12;
  private float timeLeft = 0;
  private String teamid;

  public void teamSummary(int fieldGoals, int fieldGoalAttempts, int threePointFG,
      int threePointFGAttempts, int freeThrows, int freeThrowAttempts, int offensiveRebounds,
      int defensiveRebounds, int assists, int steals, int blocks, int turnovers,
      int personalFouls) {
    this.fieldGoals += fieldGoals;
    this.fieldGoalAttempts += fieldGoalAttempts;
    this.threePointFG += threePointFG;
    this.threePointFGAttempts += threePointFGAttempts;
    this.freeThrows += freeThrows;
    this.freeThrowAttempts += freeThrowAttempts;
    this.offensiveRebounds += offensiveRebounds;
    this.defensiveRebounds += defensiveRebounds;
    this.assists += assists;
    this.steals += steals;
    this.blocks += blocks;
    this.turnovers += turnovers;
    this.personalFouls += personalFouls;
  }

  public void add2point(boolean miss) {
    fieldGoalAttempts++;
    if (!miss)
      fieldGoals++;
  }

  public void add3point(boolean miss) {
    fieldGoalAttempts++;
    threePointFGAttempts++;
    if (!miss) {
      threePointFG++;
      fieldGoals++;
    }
  }

  public void addRebound(boolean offensive) {
    if (offensive)
      offensiveRebounds++;
    else
      defensiveRebounds++;
  }

  public void addFreeThrow(boolean miss) {
    freeThrowAttempts++;
    if (!miss)
      freeThrows++;
  }


  public boolean addAssistOrBlock(boolean miss) {
    if (miss)
      blocks++;
    else
      assists++;
    return true;
  }

  public void addTurnovers() {
    turnovers++;
  }

  public boolean addSteals() {
    steals++;
    return true;
  }

  public void addFoul() {
    personalFouls++;
  }

  public void summary() {
    totalRebounds = offensiveRebounds + defensiveRebounds;
    freeThrowPercentage += freeThrows == 0 ? 0
        : ConvertHelper.roundDecimal(((double) freeThrows / freeThrowAttempts), 3);;

    threePointFGPercentage += threePointFG == 0 ? 0
        : ConvertHelper.roundDecimal(((double) threePointFG / threePointFGAttempts), 3);

    fieldGoalPercentage += fieldGoals == 0 ? 0
        : ConvertHelper.roundDecimal(((double) fieldGoals / fieldGoalAttempts), 3);

    points = freeThrows + 2 * (fieldGoals - threePointFG) + 3 * threePointFG;

  }

  public PlayerStats() {
    // TODO Auto-generated constructor stub
  }

  public void addMinutes() {
    minutesPlayed += timeEntered - timeLeft;

  }

  private boolean didNotPlay() {
    // kada budu gotovi minutesPlayed samo to proveriti je l = 0 i znaci da nije igrao
    if (assists == 0 && fieldGoalAttempts == 0 && totalRebounds == 0 && personalFouls == 0
        && steals == 0 && blocks == 0 && turnovers == 0)
      return true;
    return false;
  }

  @Override
  public String toString() {
    return didNotPlay() ? playerId + ":  " + "---------\t\tDID NOT PLAY\t\t--------"
        : playerId + "   |  " + fieldGoals + " |  " + fieldGoalAttempts + " |" + fieldGoalPercentage
            + "| " + threePointFG + " | " + threePointFGAttempts + "  |" + threePointFGPercentage
            + "  | " + freeThrows + " | " + freeThrowAttempts + "  |" + freeThrowPercentage + "| "
            + offensiveRebounds + " | " + defensiveRebounds + " | " + totalRebounds + " | "
            + assists + " | " + steals + " | " + blocks + " | " + turnovers + " | " + personalFouls
            + " | " + points + "|\n";
  }


}
