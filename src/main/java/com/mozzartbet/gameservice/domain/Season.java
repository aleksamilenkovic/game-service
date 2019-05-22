package com.mozzartbet.gameservice.domain;

import java.util.LinkedList;
import com.mozzartbet.gameservice.util.ConvertParsers;

public class Season {
  private int seasonYear;


  private final int totalTeams = 30;
  private LinkedList<Match> seasonMatches;

  public Season(String year, LinkedList<Match> matches) {
    seasonYear = ConvertParsers.tryParseInt(year) ? Integer.parseInt(year) : 0;
    seasonMatches = matches;
  }

  public int getSeasonYear() {
    return seasonYear;
  }

  public void setSeasonYear(int seasonYear) {
    this.seasonYear = seasonYear;
  }

  public LinkedList<Match> getSeasonMatches() {
    return seasonMatches;
  }

  public void setSeasonMatches(LinkedList<Match> seasonMatches) {
    this.seasonMatches = seasonMatches;
  }

  public int getTotalTeams() {
    return totalTeams;
  }


}
