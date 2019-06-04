package com.mozzartbet.gameservice.domain;

import java.util.List;
import lombok.Data;

@Data
public class Season {
  private int seasonYear;
  private List<Match> seasonMatches;
  private List<Team> teams;

  public Season(int seasonYear, List<Match> seasonMatches, List<Team> teams) {
    this.seasonYear = seasonYear;
    this.seasonMatches = seasonMatches;
    this.teams = teams;
  }


}
