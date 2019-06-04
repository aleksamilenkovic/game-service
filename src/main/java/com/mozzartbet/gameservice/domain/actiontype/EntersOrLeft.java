package com.mozzartbet.gameservice.domain.actiontype;

import com.mozzartbet.gameservice.domain.ActionType;

public class EntersOrLeft extends ActionType {
  private float timestamp = 0;

  public float getTime() {
    return timestamp;
  }

  public EntersOrLeft(String playerId, float timestamp) {
    super(playerId);
    this.timestamp = timestamp;
  }
}
