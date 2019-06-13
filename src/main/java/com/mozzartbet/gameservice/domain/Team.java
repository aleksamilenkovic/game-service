package com.mozzartbet.gameservice.domain;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class Team {
  private String teamName;
  private String teamId;
  private List<Player> players;

  public Team() {
    teamName = "";
    players = new ArrayList<Player>();
  }
}
