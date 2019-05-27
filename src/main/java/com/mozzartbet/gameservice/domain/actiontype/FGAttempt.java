package com.mozzartbet.gameservice.domain.actiontype;

import com.mozzartbet.gameservice.domain.ActionType;

public class FGAttempt extends ActionType {
  boolean miss;
  boolean threePointShoot;

  public FGAttempt(boolean miss, boolean threePointShoot, String playerId) {
    super(playerId);
    this.miss = miss;
    this.threePointShoot = threePointShoot;
  }
}
