package com.mozzartbet.gameservice.domain;

import java.util.LinkedList;

public class Match {
  private String finalScore;
  private Quarter[] quarters;
  private LinkedList<Quarter> overtimes;
  private Team firstTeam;
  private Team secondTeam;

  public Match() {
    finalScore = "0:0";
    quarters = new Quarter[4];
    overtimes = new LinkedList<Quarter>();
    firstTeam = new Team();
    secondTeam = new Team();
  }

  public String getFinalScore() {
    return finalScore;
  }

  public void setFinalScore(String finalScore) {
    this.finalScore = finalScore;
  }

  public Quarter[] getQuarters() {
    return quarters;
  }

  public void setQuarters(Quarter[] quarters) {
    this.quarters = quarters;
  }

  public LinkedList<Quarter> getOvertimes() {
    return overtimes;
  }

  public void setOvertimes(LinkedList<Quarter> overtimes) {
    this.overtimes = overtimes;
  }

  public Team getFirstTeam() {
    return firstTeam;
  }

  public void setFirstTeam(Team firstTeam) {
    this.firstTeam = firstTeam;
  }

  public Team getSecondTeam() {
    return secondTeam;
  }

  public void setSecondTeam(Team secondTeam) {
    this.secondTeam = secondTeam;
  }


}
