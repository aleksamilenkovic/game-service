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
  private Quarter quarter;
  private String scoreSummary = "";
  private float eventTime;
  private MatchEventType eventType;
  private String homeTeamAction = "";
  private String awayTeamAction = "";
  private int pointsMade = 0;
  private String neutralAction = "";
  private Player firstPlayer;
  private Player secondPlayer;

  public MatchEvent(String neutralAction, float timestamp, Quarter quarter) {
    this.neutralAction = neutralAction;
    this.eventTime = timestamp;
    this.quarter = quarter;
    eventType = null;
  }// konstruktor za neutralnu akciju (jump ball, start/end of quarter, timeout


  public static MatchEvent createForHomeTeam(String scoreSummary, float timestamp, int pointsMade,
      String action, MatchEventType type, Player players[], Quarter quarter) {
    return new MatchEvent(timestamp, scoreSummary, pointsMade, action, type, true, players,
        quarter);
  }

  public static MatchEvent createForAwayTeam(String scoreSummary, float timestamp, int pointsMade,
      String action, MatchEventType type, Player players[], Quarter quarter) {
    return new MatchEvent(timestamp, scoreSummary, pointsMade, action, type, false, players,
        quarter);
  }

  // konstruktor koji generise MatchEvent u zavisnosti da li je away ili home team napravio akciju
  private MatchEvent(float timestamp, String scoreSummary, int pointsMade, String action,
      MatchEventType type, boolean homeTeam, Player players[], Quarter quarter) {
    // boolean proverava da li je home ili away team uradio nesto
    this.homeTeamAction =
        homeTeam ? setHomeAction(action, pointsMade) : setAwayAction(action, pointsMade);
    this.eventTime = timestamp;
    this.scoreSummary = scoreSummary;
    if (players != null) {
      firstPlayer = players[0];
      secondPlayer = players[1];
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
