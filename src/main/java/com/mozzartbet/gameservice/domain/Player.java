package com.mozzartbet.gameservice.domain;

import lombok.Data;

@Data
public class Player {
  final String id;
  private String number;
  private String name;
  private String position;
  private String height;
  private String weight;
  private String birthDate;
  private String nationality;
  private int expirience;
  private String college;

  public Player(String number, String name, String position, String height, String weight,
      String birthDate, String nationality, String expirience, String college, String id) {
    this.id = id;
    this.number = number;
    this.name = name;
    this.position = position;
    this.height = height;
    this.weight = weight;
    this.birthDate = birthDate;
    this.nationality = nationality;
    if (!expirience.equals("R"))
      this.expirience = Integer.parseInt(expirience);
    else
      this.expirience = 0;
    this.college = college;
  }

}
