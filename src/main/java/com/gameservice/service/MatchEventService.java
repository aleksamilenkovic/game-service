package com.gameservice.service;

import java.util.List;
import java.util.concurrent.Future;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.gameservice.message.EventMessage;
import com.gameservice.service.dto.SendEventsRequest;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public interface MatchEventService {

	@RequiredArgsConstructor
	@Getter
	public enum MatchEventDestination {
		STATS_PLAY_BY_PLAY("stats-play-by-play");

		private final String destinationName;
	}

	// public @NotNull @Valid List<MatchEvent> findEvents(@NotNull LocalDateTime
	// fromDate,
	// @NotNull LocalDateTime toDate);

	// public void sendEvents(@NotNull LocalDateTime relativeTo,
	// @NotNull @Valid Iterable<MatchEvent> event, @NotNull MatchEventDestination
	// destination);

	// 1. Producer API: sendEvents
	// 2. Queue vs topic: as a sender, this is just a destination
	// 3. Spring config: starter + bean for queues
	// 4. How does Spring know how to prepare and send our domain object?
	// 5. JSON & Jackson
	// 6. Async; recieve timeouts

	public @NotNull Future<List<EventMessage>> sendEvents(@NotNull @Valid SendEventsRequest request);
}
