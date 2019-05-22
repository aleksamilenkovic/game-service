package com.mozzartbet.gameservice.domain;


public class MatchEvent {
  private MatchEventType type;
  private String scoreSummary;
  private String timestamp;
  private String firstTeamAction;
  private String secondTeamAction;
  private String pointsMadeInAction;

  public MatchEventType getType() {
    return type;
  }

  public void setType(MatchEventType type) {
    this.type = type;
  }

  public String getScoreSummary() {
    return scoreSummary;
  }

  public void setScoreSummary(String scoreSummary) {
    this.scoreSummary = scoreSummary;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public String getFirstTeamAction() {
    return firstTeamAction;
  }

  public void setFirstTeamAction(String firstTeamAction) {
    this.firstTeamAction = firstTeamAction;
  }

  public String getSecondTeamAction() {
    return secondTeamAction;
  }

  public void setSecondTeamAction(String secondTeamAction) {
    this.secondTeamAction = secondTeamAction;
  }

  public String getPointsMadeInAction() {
    return pointsMadeInAction;
  }

  public void setPointsMadeInAction(String pointsMadeInAction) {
    this.pointsMadeInAction = pointsMadeInAction;
  }



}
