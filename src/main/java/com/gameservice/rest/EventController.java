package com.gameservice.rest;

import static java.time.LocalDateTime.parse;
import static java.util.Arrays.asList;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gameservice.domain.Match;
import com.gameservice.domain.MatchEvent;
import com.gameservice.domain.MatchEventType;
import com.gameservice.domain.Player;
import com.gameservice.domain.Quarter;
import com.gameservice.domain.Season;
import com.gameservice.domain.Team;
import com.gameservice.mapper.TeamMapper;
import com.gameservice.repository.PlayerRepository;
import com.gameservice.repository.SeasonRepository;
import com.gameservice.service.MatchEventService;
import com.gameservice.service.MatchEventService.MatchEventDestination;
import com.gameservice.service.MatchService;
import com.gameservice.service.dto.SendEventsRequest;

@RestController
public class EventController {

	@Autowired
	private TeamMapper teamMapper;

	@Autowired
	private PlayerRepository playerRepo;
	@Autowired
	private SeasonRepository seasonRepo;
	@Autowired
	private MatchService matchService;
	@Autowired
	private MatchEventService eventService;

	@PostMapping(path = "/team/generate")
	public void generateTeam() {
		Season s = Season.builder().seasonYear(1910).build();
		seasonRepo.insert(s);
		Team t = Team.builder().name("KK Toplicanin").teamId("TOP/1910").seasonYear(1910).build();
		teamMapper.insert(t);

		Player p1 = Player.builder().team(t).name("Marko Petrovic").playerId("mare/p").number("15").position("C")
				.build();
		playerRepo.insert(p1);

		Player p2 = Player.builder().team(t).name("Jovan Djokovic").playerId("joca/dj").number("27").position("SG")
				.build();
		playerRepo.insert(p2);
	}

	@PostMapping(path = "/events/generate")
	public void generateEvents() {
		Player p1 = playerRepo.findPlayersByName("Marko Petrovic", null).get(0);
		Player p2 = playerRepo.findPlayersByName("Jovan Djokovic", null).get(0);
		Match match = Match.builder().matchId("1910TOPPK").quarters(new ArrayList<Quarter>()).build();
		Quarter q = Quarter.builder().name("1st Q").match(match).build();
		match.getQuarters().add(q);

		List<MatchEvent> es = asList(
				MatchEvent.builder().eventTimestamp(parse("2019-05-01T11:12:13")).quarter(q).firstPlayer(p1)
						.pointsMade(2).eventType(MatchEventType.SHOOTFOR2).build(),
				MatchEvent.builder().eventTimestamp(parse("2019-05-01T11:13:05")).quarter(q).firstPlayer(p2)
						.pointsMade(3).eventType(MatchEventType.SHOOTFOR3).build(),
				MatchEvent.builder().eventTimestamp(parse("2019-05-01T11:13:17")).quarter(q).firstPlayer(p1)
						.pointsMade(0).secondPlayer(p2).eventType(MatchEventType.TURNOVER).build());
		q.setMatchEvents(es);
		/*
		 * es.forEach(e -> { try { eventRepo.insert(e); } catch
		 * (SQLIntegrityConstraintViolationException e1) { // TODO Auto-generated catch
		 * block e1.printStackTrace(); } });
		 */
		matchService.save(match);
	}

	@PostMapping(path = "/events/send")
	public void sendEvents() {
		eventService.sendEvents(SendEventsRequest.builder().destination(MatchEventDestination.STATS_PLAY_BY_PLAY)
				.fromDate(LocalDateTime.parse("2019-05-01T11:12:00")).toDate(LocalDateTime.parse("2019-05-01T11:14:00"))
				.relativeTo(LocalDateTime.parse("2019-05-01T11:12:00")).speedFactor(0.1d).build());
	}
}
