package com.mozzartbet.gameservice.domain.actiontype;

import com.mozzartbet.gameservice.domain.ActionType;

public class Rebound extends ActionType {
  ReboundType rebType;

  public ReboundType getRebType() {
    return rebType;
  }

  public Rebound(ReboundType rebType, String playerId) {
    super(playerId);
    this.rebType = rebType;
  }

}
