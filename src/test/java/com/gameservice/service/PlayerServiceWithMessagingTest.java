package com.gameservice.service;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

import com.gameservice.domain.Player;
import com.gameservice.domain.Team;
import com.gameservice.mapper.setups.PlayerSetup;
import com.gameservice.service.PlayerService.PlayerDestination;
import com.gameservice.service.dto.PlayerSearchResponse;
import com.gameservice.service.exceptions.PlayerException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PlayerServiceWithMessagingTest extends BaseServiceTest {

	@Autowired
	private PlayerSetup playerSetup;

	@Autowired
	private PlayerService playerService;

	@Autowired
	private JmsTemplate jmsTemplate;

	//

	PlayerSearchResponse psr;

	@Rule
	public final ExpectedException thrown = ExpectedException.none();

	@Test
	public void testSavePlayer_Duplicated() {
		Team t = playerSetup.teamToplicanin1910();

		Player p1 = Player.builder().team(t).name("Nikola Jokic").playerId("jokic/n").number("15").position("C")
				.build();
		playerService.save(p1);

		jmsTemplate.setReceiveTimeout(1000);

		Player mp1 = (Player) jmsTemplate.receiveAndConvert(PlayerDestination.CREATED.getName());
		assertThat(mp1.getName(), equalTo("Nikola Jokic"));

		// thrown.expect(PlayerException.class);
		// thrown.expectMessage("[DUPLICATED_NAME] Name: Nikola Jokic is duplicated!");
		//
		// // custom matcher, more reusable
		// thrown.expect(new ApplicationExceptionMatcher<>(PlayerException.class,
		// DUPLICATED_NAME,
		// "[DUPLICATED_NAME]
		// Name: Nikola Jokic is duplicated!"));

		Player p3 = Player.builder().team(t).name("Nikola Jokic").playerId("jokic/n").number("4").position("PG")
				.build();
		try {
			playerService.save(p3);
			fail("Managed to save, but I shouldn't: " + p3);
		} catch (PlayerException expected) {
			// all good
		}

		Object what = jmsTemplate.receiveAndConvert(PlayerDestination.CREATED.getName());
		log.info("Expecting to get null for: {}", what);
		assertNull(what);
	}

}
