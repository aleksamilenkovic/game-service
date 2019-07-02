package com.mozzartbet.gameservice.domain;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Team implements BaseEntity {
  @EqualsAndHashCode.Include
  private Long id;
  private LocalDateTime createdOn;
  private LocalDateTime modifiedOn;
  private String name;
  private String teamId;
  private int seasonYear;
  private List<Player> players;

}
