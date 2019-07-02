package com.mozzartbet.gameservice.domain.boxscore;

import java.time.LocalDateTime;
import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.domain.Team;
import com.mozzartbet.gameservice.util.ConvertHelper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
public class PlayerStats {
  public PlayerStats(String playerid, Team team, Match match) {
    this.player = playerid == null ? null : Player.builder().playerId(playerid).team(team).build();
    this.team = team;
    this.match = match;
  }

  @EqualsAndHashCode.Include
  private Long id;
  private LocalDateTime createdOn;
  private LocalDateTime modifiedOn;
  private Player player;
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
  private Match match;
  private Team team;

  public void teamSummary(PlayerStats ps) {
    this.fieldGoals += ps.getFieldGoals();
    this.fieldGoalAttempts += ps.getFieldGoalAttempts();
    this.threePointFG += ps.getThreePointFG();
    this.threePointFGAttempts += ps.getThreePointFGAttempts();
    this.freeThrows += ps.getFreeThrows();
    this.freeThrowAttempts += ps.getFreeThrowAttempts();
    this.offensiveRebounds += ps.getOffensiveRebounds();
    this.defensiveRebounds += ps.getDefensiveRebounds();
    this.assists += ps.getAssists();
    this.steals += ps.getSteals();
    this.blocks += ps.getBlocks();
    this.turnovers += ps.getTurnovers();
    this.personalFouls += ps.getPersonalFouls();
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
    return didNotPlay() ? player + ":  " + "---------\t\tDID NOT PLAY\t\t--------"
        : player + "   |  " + fieldGoals + " |  " + fieldGoalAttempts + " |" + fieldGoalPercentage
            + "| " + threePointFG + " | " + threePointFGAttempts + "  |" + threePointFGPercentage
            + "  | " + freeThrows + " | " + freeThrowAttempts + "  |" + freeThrowPercentage + "| "
            + offensiveRebounds + " | " + defensiveRebounds + " | " + totalRebounds + " | "
            + assists + " | " + steals + " | " + blocks + " | " + turnovers + " | " + personalFouls
            + " | " + points + "|\n";
  }


}
