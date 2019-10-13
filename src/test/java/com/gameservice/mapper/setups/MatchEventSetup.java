package com.gameservice.mapper.setups;

import static java.time.LocalDateTime.parse;
import static java.util.Arrays.asList;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gameservice.domain.Match;
import com.gameservice.domain.MatchEvent;
import com.gameservice.domain.MatchEventType;
import com.gameservice.domain.Player;
import com.gameservice.domain.Quarter;
import com.gameservice.domain.Team;
import com.gameservice.mapper.MatchEventMapper;
import com.gameservice.mapper.MatchMapper;
import com.gameservice.mapper.PlayerMapper;
import com.gameservice.mapper.QuarterMapper;
import com.gameservice.mapper.TeamMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MatchEventSetup {

	@Autowired
	private MatchEventMapper eventMapper;
	@Autowired
	private PlayerMapper playerMapper;
	@Autowired
	private TeamMapper teamMapper;
	@Autowired
	private MatchMapper matchMapper;
	@Autowired
	private QuarterMapper quarterMapper;

	public List<MatchEvent> eventsForSending(List<Player> ps) {
		Player p1 = ps.get(0);
		Player p2 = ps.get(1);
		Team t2 = Team.builder().name("KK Prokuplje").teamId("PK/1910").seasonYear(1910).build();
		teamMapper.insert(t2);
		Player p = Player.builder().team(t2).name("Nikola Arnautovic").number("14").birthDate(LocalDateTime.now())
				.college("University of Belgrade").experience(4).playerId("dzonii").position("FG").height("7\"2")
				.weight("205lbs").nationality("srb").build();
		playerMapper.insert(p);
		Match m = Match.builder().matchId("SAC21015154").awayTeam(p1.getTeam()).homeTeam(t2).finalScore("119-120")
				.dateTime(LocalDateTime.now().minusDays(30)).awayTeamPoints(119).build();
		matchMapper.insert(m);
		Quarter quarter = Quarter.builder().match(m).name("1st Q").pointsAwayTeam(119).pointsHomeTeam(110).build();
		quarterMapper.insert(quarter);
		/*
		 * 
		 * MatchEvent event =
		 * MatchEvent.builder().quarter(quarter).awayTeamAction("afafaeefaa")
		 * .pointsMade(3).firstPlayer(p).eventType(MatchEventType.FOUL).eventTime(31F).
		 * build();
		 */

		log.info("Saving events");
		List<MatchEvent> es = asList(
				MatchEvent.builder().quarter(quarter).awayTeamAction(String.format("FOUL MADE BY %s", p.getName()))
						.pointsMade(0).firstPlayer(p1).eventType(MatchEventType.FOUL).eventTime(5.2f)
						.eventTimestamp(parse("2019-05-01T11:12:59")).build(),
				MatchEvent.builder().quarter(quarter).awayTeamAction("Marko made 3 point (assist from Jovan)")
						.pointsMade(3).firstPlayer(p1).secondPlayer(p2).eventType(MatchEventType.SHOOTFOR3)
						.eventTimestamp(parse("2019-05-01T11:13:02")).build(),
				MatchEvent.builder().quarter(quarter).awayTeamAction("Second free throw made by " + p2.getName())
						.pointsMade(1).firstPlayer(p2).eventType(MatchEventType.FREETHROW)
						.eventTimestamp(parse("2019-05-01T11:13:05")).build());

		es.forEach(e -> {
			eventMapper.insert(e);
		});

		return es;
	}

	public long countSent(int sent) {
		return eventMapper.countSent(sent);
	}
}
