package com.mozzartbet.gameservice.domain;

import java.util.LinkedList;
import lombok.Data;

@Data
public class Match {

  // private Quarter[] quarters;
  // private LinkedList<Quarter> overtimes;
  private String date;
  private String awayTeam;
  private String finalScore;
  private String homeTeam;
  private LinkedList<MatchEvent> matchEvents;
  private String homeTeamPoints;
  private String awayTeamPoints;
  // za pocetak nek timovi budu stringovi, kasnije klase timovi (a vec su napravljene metode za
  // vracanje timova)


  public Match() {
    finalScore = "0:0";
    // quarters = new Quarter[4];
    // overtimes = new LinkedList<Quarter>();
    homeTeam = "HomeTeam";
    awayTeam = "AwayTeam";
    homeTeamPoints = "0";
    awayTeamPoints = "0";
    matchEvents = new LinkedList<MatchEvent>();
    date = "";
  }

  public Match(String date, String awayTeam, String awayTeamPoints, String homeTeam,
      String homeTeamPoints, LinkedList<MatchEvent> matchEvents) {
    this.date = date;
    this.homeTeam = homeTeam;
    this.awayTeam = awayTeam;
    this.homeTeamPoints = homeTeamPoints;
    this.awayTeamPoints = awayTeamPoints;
    this.matchEvents = matchEvents;
    this.finalScore = this.awayTeamPoints + "-" + this.homeTeamPoints;
  }

  @Override
  public String toString() {
    return "Match [date=" + date + ", awayTeam=" + awayTeam + ", finalScore=" + finalScore
        + ", homeTeam=" + homeTeam + ", matchEvents=" + matchEvents + "]";
  }

}