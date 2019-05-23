package com.mozzartbet.gameservice.domain;

import java.util.LinkedList;
import lombok.Data;

@Data
public class Season {
  private int seasonYear;
  private LinkedList<Match> seasonMatches;
  private LinkedList<Team> teams;

  public Season(int seasonYear, LinkedList<Match> seasonMatches, LinkedList<Team> teams) {
    this.seasonYear = seasonYear;
    this.seasonMatches = seasonMatches;
    this.teams = teams;
  }


}
