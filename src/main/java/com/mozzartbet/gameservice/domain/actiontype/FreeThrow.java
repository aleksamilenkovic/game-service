package com.mozzartbet.gameservice.domain.actiontype;

import com.mozzartbet.gameservice.domain.ActionType;

public class FreeThrow extends ActionType {
  private boolean miss;

  public boolean isMiss() {
    return miss;
  }

  public FreeThrow(boolean miss, String playerId) {
    super(playerId);
    this.miss = miss;
  }
}
