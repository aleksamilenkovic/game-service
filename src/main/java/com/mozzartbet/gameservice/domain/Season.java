package com.mozzartbet.gameservice.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Season {
  private int seasonYear;
  private List<Match> seasonMatches;
  private List<Team> teams;
}
