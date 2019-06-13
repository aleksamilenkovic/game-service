package com.mozzartbet.gameservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Player {
  private final String player_id;
  private String team_id;
  private String number;
  private String name;
  private String position;
  private String height;
  private String weight;
  private String birthDate;
  private String nationality;
  private int expirience;
  private String college;


}
