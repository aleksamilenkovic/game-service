package com.mozzartbet.gameservice.domain;

import lombok.Data;

@Data
public class MatchEvent {
  private MatchEventType type;
  private String scoreSummary;
  private String timestamp;
  private String firstTeamAction;
  private String secondTeamAction;
  private String pointsMadeInAction;



}
