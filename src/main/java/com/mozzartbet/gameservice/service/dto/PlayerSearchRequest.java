package com.mozzartbet.gameservice.service.dto;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class PlayerSearchRequest {

  @Pattern(regexp = "\\w+")
  String playerId;

  @NotNull
  @Pattern(regexp = "\\w+")
  String playerName;

  @Min(1)
  Long teamPK;

  @Pattern(regexp = "\\w+")
  String teamId;

  @Pattern(regexp = "\\w+")
  String teamName;

}
