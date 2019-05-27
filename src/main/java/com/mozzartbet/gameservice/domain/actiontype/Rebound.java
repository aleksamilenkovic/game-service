package com.mozzartbet.gameservice.domain.actiontype;

import com.mozzartbet.gameservice.domain.ActionType;

public class Rebound extends ActionType {
  ReboundType type;

  public Rebound(ReboundType type, String playerId) {
    super(playerId);
    this.type = type;
  }
}
