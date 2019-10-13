package com.gameservice.message;

import java.time.LocalDateTime;

import com.gameservice.domain.MatchEventType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@ToString
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EventMessage {

	@EqualsAndHashCode.Include
	private Long matchId;
	@EqualsAndHashCode.Include
	private Long firstTeamId;
	@EqualsAndHashCode.Include
	private Long secondTeamId;
	@EqualsAndHashCode.Include
	private Long firstPlayerId;
	@EqualsAndHashCode.Include
	private Long secondPlayerId;
	@EqualsAndHashCode.Include
	private MatchEventType eventType;
	@EqualsAndHashCode.Include
	private LocalDateTime eventTimestamp;
	@EqualsAndHashCode.Include
	private int pointsMade;

}
