package com.mozzartbet.gameservice.domain;

import java.util.LinkedList;

public class Team {
  private String teamName;
  private LinkedList<Player> players;

  public Team() {
    teamName = "";
    players = new LinkedList<Player>();
  }

  public String getTeamName() {
    return teamName;
  }

  public void setTeamName(String teamName) {
    this.teamName = teamName;
  }

  public LinkedList<Player> getPlayers() {
    return players;
  }

  public void setPlayers(LinkedList<Player> players) {
    this.players = players;
  }

}
