package com.mozzartbet.gameservice.domain;

import java.util.LinkedList;
import lombok.Data;

@Data
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


}
