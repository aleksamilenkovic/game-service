package com.mozzartbet.gameservice.domain.boxscore;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PlayerStats {
  public PlayerStats() {
    // TODO Auto-generated constructor stub
  }

  private String playerId;
  private int fieldGoals = 0;
  private int fieldGoalAttempts = 0;
  private double fildGoalPercentage = 0;
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


}
