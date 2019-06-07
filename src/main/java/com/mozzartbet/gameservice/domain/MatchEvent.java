package com.mozzartbet.gameservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchEvent {
  // private MatchEventType type;
  private String scoreSummary = "";
  private float timestamp;
  // mogucnost za 2 tipa u jednoj akciji (npr pogodak i asistencija)
  // [VAZNO] pri prolazu kroz akciju (te 2) ako je prva rebound drugu PRESKOCITI jer je prazan
  // element. Takodje ako su akcije null znaci da je timski nesto uradjeno ili timeout
  private ActionType actions[] = null;
  private String homeTeamAction = "";
  private String awayTeamAction = "";
  private int pointsMadeHomeTeam = 0;
  private int pointsMadeAwayTeam = 0;
  private String neutralAction = "";
  private String quarter;
  private int resultHomeLead;// ovaj field sluzice kasnije za racunanje plus minus;

  public MatchEvent(String neutralAction, float timestamp, String quarter) {
    this.neutralAction = neutralAction;
    this.timestamp = timestamp;
    this.quarter = quarter;
  }// konstruktor za neutralnu akciju (jump ball, start/end of quarter, timeout


  public static MatchEvent createForHomeTeam(String scoreSummary, float timestamp, int pointsMade,
      String action, ActionType actions[], String quarter) {
    return new MatchEvent(timestamp, scoreSummary, pointsMade, action, actions, quarter, true);
  }

  public static MatchEvent createForAwayTeam(String scoreSummary, float timestamp, int pointsMade,
      String action, ActionType actions[], String quarter) {
    return new MatchEvent(timestamp, scoreSummary, pointsMade, action, actions, quarter, false);
  }

  // konstruktor koji generise MatchEvent u zavisnosti da li je away ili home team napravio akciju
  private MatchEvent(float timestamp, String scoreSummary, int pointsMade, String action,
      ActionType actions[], String quarter, boolean homeTeam) {
    // boolean proverava da li je home ili away team uradio nesto
    this.homeTeamAction =
        homeTeam ? setHomeAction(action, pointsMade) : setAwayAction(action, pointsMade);
    this.timestamp = timestamp;
    this.scoreSummary = scoreSummary;
    String result[] = scoreSummary.split("-");
    resultHomeLead = Integer.parseInt(result[0]) - Integer.parseInt(result[1]);
    this.actions = actions;
    this.quarter = quarter;
  }

  private String setHomeAction(String action, int pointsMade) {
    this.pointsMadeHomeTeam = pointsMade;
    return action;
  }

  private String setAwayAction(String action, int pointsMade) {
    this.awayTeamAction = action;
    this.pointsMadeAwayTeam = pointsMade;
    return "";
  }
}
