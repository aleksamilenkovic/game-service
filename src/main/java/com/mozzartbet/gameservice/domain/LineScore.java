package com.mozzartbet.gameservice.domain;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class LineScore implements BaseEntity {
  @EqualsAndHashCode.Include
  private Long id;
  private LocalDateTime createdOn;
  private LocalDateTime modifiedOn;
  private Match match;
  private String quarter_name;
  private Team team;
  private int points;
}
