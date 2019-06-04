package com.mozzartbet.gameservice.domain;

import java.util.List;
import lombok.Data;

@Data
public class Quarter {
  public Quarter(String quarter, List<MatchEvent> quarterEvents, String scoreSummary) {
    this.quarterName = quarter;
    this.matchEvents = quarterEvents;
    String result[] = scoreSummary.split("-");
    this.pointsAwayTeam = Integer.parseInt(result[0]);
    this.pointsHomeTeam = Integer.parseInt(result[1]);
  }

  private int pointsHomeTeam = 0;
  private int pointsAwayTeam = 0;
  private List<MatchEvent> matchEvents;
  private String quarterName;
}
