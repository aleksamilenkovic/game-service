package com.gameservice.service;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.jms.JMSException;
import javax.jms.Message;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.json.JSONException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.jdbc.Sql;

import com.gameservice.domain.Player;
import com.gameservice.mapper.setups.MatchEventSetup;
import com.gameservice.mapper.setups.PlayerSetup;
import com.gameservice.message.EventMessage;
import com.gameservice.service.MatchEventService.MatchEventDestination;
import com.gameservice.service.dto.SendEventsRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MatchEventServiceSendingTest extends BaseSpringTest {
	@Autowired
	private PlayerSetup playerSetup;

	@Autowired
	private MatchEventSetup eventSetup;

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private MatchEventService eventService;

	@Test
	// @Transactional
	@Sql(executionPhase = AFTER_TEST_METHOD, statements = { "delete from basketball_seasons",
			"delete from basketball_matches", "delete from basketball_players", "delete from basketball_teams" })
	public void sendEventsAndManuallyRecieve() throws JMSException, JSONException {
		List<Player> ps = playerSetup.toplicanin1910();
		eventSetup.eventsForSending(ps);

		eventService.sendEvents(SendEventsRequest.builder().destination(MatchEventDestination.STATS_PLAY_BY_PLAY)
				.fromDate(LocalDateTime.parse("2019-05-01T11:12:00")).toDate(LocalDateTime.parse("2019-05-01T11:14:00"))
				.relativeTo(LocalDateTime.parse("2019-05-01T11:12:00")).speedFactor(0.1d).build());

		// good practice, if bug causes no message to be dispatched
		jmsTemplate.setReceiveTimeout(10_000);

		String tId = ps.get(0).getTeam().getTeamId();
		String p1Id = ps.get(0).getPlayerId();
		String p2Id = ps.get(1).getPlayerId();

		Message m;

		m = jmsTemplate.receive(MatchEventDestination.STATS_PLAY_BY_PLAY.getDestinationName());
		log.info("{}", ((ActiveMQTextMessage) m).getText());
		// JSONAssert
		assertEquals(String.format(
				"{'id':1,'firstPlayerId':'%s','firstTeamId':'%s','matchId':'%s','eventTimestamp':'2019-05-01T11:12:59','eventType':'FOUL'}",
				p1Id, tId, "SAC21015154"), ((ActiveMQTextMessage) m).getText(), false);

		m = jmsTemplate.receive(MatchEventDestination.STATS_PLAY_BY_PLAY.getDestinationName());
		assertEquals(String.format(
				"{'id':2,'eventTimestamp':'2019-05-01T11:13:02','eventType':'SHOOTFOR3','firstPlayerId':'%s','secondPlayerId':'%s','firstTeamId':'%s','secondTeamId':'%s','matchId':'%s'}",
				p1Id, p2Id, tId, tId, "SAC21015154"), ((ActiveMQTextMessage) m).getText(), false);

		m = jmsTemplate.receive(MatchEventDestination.STATS_PLAY_BY_PLAY.getDestinationName());
		assertEquals(String.format(
				"{'id':3,'eventTimestamp':'2019-05-01T11:13:05','eventType':'FREETHROW','firstPlayerId':'%s','firstTeamId':'%s','matchId':'%s'}",
				p2Id, tId, "SAC21015154"), ((ActiveMQTextMessage) m).getText(), false);
	}

	@Test
	// @Transactional
	@Sql(executionPhase = AFTER_TEST_METHOD, statements = { "delete from basketball_seasons",
			"delete from basketball_matches", "delete from basketball_players", "delete from basketball_teams" })
	public void sendEventsAndManuallyRecieveAndConvert()
			throws JMSException, JSONException, InterruptedException, ExecutionException {
		List<Player> ps = playerSetup.toplicanin1910();
		eventSetup.eventsForSending(ps);
		assertThat(eventSetup.countSent(0), equalTo(3L));

		Future<List<EventMessage>> es;
		// add async

		es = eventService.sendEvents(SendEventsRequest.builder().destination(MatchEventDestination.STATS_PLAY_BY_PLAY)
				.fromDate(LocalDateTime.parse("2019-05-01T11:12:00")).toDate(LocalDateTime.parse("2019-05-01T11:14:00"))
				.relativeTo(LocalDateTime.parse("2019-05-01T11:12:00")).speedFactor(0.1).build());

		// good practice, if bug causes no message to be dispatched
		jmsTemplate.setReceiveTimeout(60_000);

		// How does it know to create proper object?
		List<EventMessage> res = new ArrayList<>();
		res.add((EventMessage) jmsTemplate
				.receiveAndConvert(MatchEventDestination.STATS_PLAY_BY_PLAY.getDestinationName()));
		res.add((EventMessage) jmsTemplate
				.receiveAndConvert(MatchEventDestination.STATS_PLAY_BY_PLAY.getDestinationName()));
		res.add((EventMessage) jmsTemplate
				.receiveAndConvert(MatchEventDestination.STATS_PLAY_BY_PLAY.getDestinationName()));

		assertEquals(res, es.get());

		// let's check the database
		assertThat(eventSetup.countSent(1), equalTo(3L));
	}

}
