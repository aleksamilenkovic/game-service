package com.mozzartbet.gameservice.domain;

import java.time.LocalDateTime;
import java.util.List;
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
public class Season {
  @EqualsAndHashCode.Include
  private long id;

  private LocalDateTime createdOn;
  private LocalDateTime modifiedOn;
  private int seasonYear;
  private List<Match> seasonMatches;
  private List<Team> teams;
}
