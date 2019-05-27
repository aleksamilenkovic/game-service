package com.mozzartbet.gameservice.domain;

import com.mozzartbet.gameservice.domain.actiontype.OtherType;

public class ActionType {
  protected String playerId;
  protected OtherType type;

  public ActionType(String playerId, OtherType type) {
    this.playerId = playerId;
    this.type = type;
  }

  public ActionType(String playerId) {
    this.playerId = playerId;
  }

  public String getPlayerId() {
    return playerId;
  }


  public OtherType getType() {
    return type;
  }

}
