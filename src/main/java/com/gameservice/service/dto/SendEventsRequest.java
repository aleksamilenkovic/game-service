package com.gameservice.service.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import com.gameservice.service.MatchEventService.MatchEventDestination;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class SendEventsRequest {

	@NotNull
	final MatchEventDestination destination;
	@NotNull
	final LocalDateTime relativeTo;
	@NotNull
	final LocalDateTime fromDate;
	@NotNull
	final LocalDateTime toDate;

	Double speedFactor;

	public String getDestinationName() {
		return destination.getDestinationName();
	}

	public long calculateTimeToSleep(long timeToSleep) {
		return (speedFactor == null) ? timeToSleep : Math.round(timeToSleep * speedFactor);
	}
}
