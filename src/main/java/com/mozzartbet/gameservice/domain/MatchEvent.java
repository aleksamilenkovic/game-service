package com.mozzartbet.gameservice.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MatchEvent implements BaseEntity {
  @EqualsAndHashCode.Include
  private Long id;

  private LocalDateTime createdOn;
  private LocalDateTime modifiedOn;
  private String matchId;
  private String scoreSummary = "";
  private float timestamp;
  private MatchEventType eventType;
  private String homeTeamAction = "";
  private String awayTeamAction = "";
  private int pointsMade = 0;
  private String neutralAction = "";
  private String quarter;
  private String firstPlayerId = null;
  private String secondPlayerId = null;

  public MatchEvent(String neutralAction, float timestamp, String quarter) {
    this.neutralAction = neutralAction;
    this.timestamp = timestamp;
    this.quarter = quarter;
  }// konstruktor za neutralnu akciju (jump ball, start/end of quarter, timeout


  public static MatchEvent createForHomeTeam(String scoreSummary, float timestamp, int pointsMade,
      String action, MatchEventType type, String quarter, String players[]) {
    return new MatchEvent(timestamp, scoreSummary, pointsMade, action, type, quarter, true,
        players);
  }

  public static MatchEvent createForAwayTeam(String scoreSummary, float timestamp, int pointsMade,
      String action, MatchEventType type, String quarter, String players[]) {
    return new MatchEvent(timestamp, scoreSummary, pointsMade, action, type, quarter, false,
        players);
  }

  // konstruktor koji generise MatchEvent u zavisnosti da li je away ili home team napravio akciju
  private MatchEvent(float timestamp, String scoreSummary, int pointsMade, String action,
      MatchEventType type, String quarter, boolean homeTeam, String players[]) {
    // boolean proverava da li je home ili away team uradio nesto
    this.homeTeamAction =
        homeTeam ? setHomeAction(action, pointsMade) : setAwayAction(action, pointsMade);
    this.timestamp = timestamp;
    this.scoreSummary = scoreSummary;
    if (players != null) {
      firstPlayerId = players[0];
      secondPlayerId = players[1];
    }
    // String result[] = scoreSummary.split("-");
    // resultHomeLead = Integer.parseInt(result[0]) - Integer.parseInt(result[1]);
    this.eventType = type;
    this.quarter = quarter;
  }

  private String setHomeAction(String action, int pointsMade) {
    this.pointsMade = pointsMade;
    return action;
  }

  private String setAwayAction(String action, int pointsMade) {
    this.awayTeamAction = action;
    this.pointsMade = pointsMade;
    return "";
  }

  public boolean offensiveRebound() {
    return homeTeamAction.contains("Offensive") ? true
        : false || awayTeamAction.contains("Offensive") ? true : false;
  }
}
