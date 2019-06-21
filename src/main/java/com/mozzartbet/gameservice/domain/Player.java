package com.mozzartbet.gameservice.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Player implements BaseEntity {
  @EqualsAndHashCode.Include
  private Long id;
  private String playerId;
  private Team team;
  private LocalDateTime createdOn;
  private LocalDateTime modifiedOn;
  private String number;
  private String name;
  private String position;
  private String height;
  private String weight;
  private LocalDateTime birthDate;
  private String nationality;
  private int experience;
  private String college;



}
