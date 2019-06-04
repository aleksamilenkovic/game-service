package com.mozzartbet.gameservice.domain;

import lombok.Data;

@Data
public class MatchEvent {
  // private MatchEventType type;
  private String scoreSummary = "";
  private float timestamp;
  // mogucnost za 2 tipa u jednoj akciji (npr pogodak i asistencija)
  // [VAZNO] pri prolazu kroz akciju (te 2) ako je prva rebound drugu PRESKOCITI jer je prazan
  // element. Takodje ako su akcije null znaci da je timski nesto uradjeno ili timeout
  private ActionType actions[];
  private String homeTeamAction = "";
  private String awayTeamAction = "";
  private int pointsMadeHomeTeam = 0;
  private int pointsMadeAwayTeam = 0;
  private String neutralAction = "";
  private String quarter;
  private int resultHomeLead;

  public MatchEvent(String neutralAction, float timestamp, String quarter) {
    this.neutralAction = neutralAction;
    this.timestamp = timestamp;
    this.quarter = quarter;
    actions = null;
  }

  // konstruktor za drugi tim tj homeTeam ako je nesto uradio
  public MatchEvent(float timestamp, String scoreSummary, int pointsMadeHomeTeam,
      String homeTeamAction, ActionType actions[], String quarter) {
    this.timestamp = timestamp;
    this.scoreSummary = scoreSummary;
    String result[] = scoreSummary.split("-");
    resultHomeLead = Integer.parseInt(result[0]) - Integer.parseInt(result[1]);
    this.pointsMadeHomeTeam = pointsMadeHomeTeam;
    this.homeTeamAction = homeTeamAction;
    this.actions = actions;
    this.quarter = quarter;
  }

  // konstruktor za prvi tim ako je nesto uradio
  public MatchEvent(float timestamp, String awayTeamAction, ActionType actions[],
      int pointsMadeAwayTeam, String scoreSummary, String quarter) {
    this.timestamp = timestamp;
    this.awayTeamAction = awayTeamAction;
    this.actions = actions;
    this.pointsMadeAwayTeam = pointsMadeAwayTeam;
    this.scoreSummary = scoreSummary;
    String result[] = scoreSummary.split("-");
    resultHomeLead = Integer.parseInt(result[1]) - Integer.parseInt(result[0]);
    this.quarter = quarter;
  }

}
