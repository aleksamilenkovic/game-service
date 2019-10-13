package com.gameservice.message;

import org.springframework.stereotype.Component;

import com.gameservice.domain.MatchEvent;

@Component
public class EventMessageConverter {
	// private static final long HARDCODED_EXAMPLE_MATCH_ID = 1002L;

	void populate(EventMessage e, MatchEvent m) {
		// e.setId(m.getId());
		e.setEventTimestamp(m.getEventTimestamp());
		if (m.getFirstPlayer() != null) {
			e.setFirstPlayerId(m.getFirstPlayer().getId());
			e.setFirstTeamId(m.getFirstPlayer().getTeam().getId());
		}
		if (m.getSecondPlayer() != null) {
			e.setSecondPlayerId(m.getSecondPlayer().getId());
			e.setSecondTeamId(m.getSecondPlayer().getTeam().getId());
		}
		e.setEventType(m.getEventType());
		e.setMatchId(m.getQuarter().getMatch().getId());
		e.setPointsMade(m.getPointsMade());
	}

	public EventMessage convert(MatchEvent source) {
		EventMessage e = new EventMessage();
		populate(e, source);
		return e;
	}
}
