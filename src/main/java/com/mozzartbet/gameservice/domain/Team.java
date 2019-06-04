package com.mozzartbet.gameservice.domain;

import java.util.LinkedList;
import java.util.List;
import lombok.Data;

@Data
public class Team {
  private String teamName;
  private String teamId;
  private List<Player> players;

  public Team() {
    teamName = "";
    players = new LinkedList<Player>();
  }



  @Override
  public String toString() {
    return "Team [teamName=" + teamName + ", players=" + players + "]";
  }

  public void showTeam() {
    System.out.println("=====" + teamName + "=====");
    for (Player p : players) {
      System.out.println(p);
    }
  }
}
