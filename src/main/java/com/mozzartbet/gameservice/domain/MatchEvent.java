package com.mozzartbet.gameservice.domain;

import lombok.Data;

@Data
public class MatchEvent {
  private MatchEventType type;
  private String scoreSummary;
  private String timestamp;
  private String homeTeamAction;
  private String awayTeamAction;
  private String pointsMadeHomeTeam;
  private String pointsMadeAwayTeam;
  private String neutralAction;
  private String quarter;

  public MatchEvent(MatchEventType type, String neutralAction, String timestamp, int quarter) {
    this.type = type;
    this.neutralAction = neutralAction;
    this.timestamp = timestamp;
    if (quarter < 5)
      this.quarter = quarter + ".quarter";
    else
      this.quarter = quarter - 4 + ".OT";
  }

  public MatchEvent(MatchEventType type, String action, String points, String timestamp,
      int quarter, String scoreSummary) {
    if (type == MatchEventType.SCOREHOMETEAM) {
      homeTeamAction = action;
      pointsMadeHomeTeam = points;
    } else if (type == MatchEventType.SCOREAWAYTEAM) {
      awayTeamAction = action;
      pointsMadeAwayTeam = points;
    }
    this.type = type;
    this.timestamp = timestamp;
    this.scoreSummary = scoreSummary;
    if (quarter < 5)
      this.quarter = quarter + ".quarter";
    else
      this.quarter = quarter - 4 + ".OT";
  }

}
