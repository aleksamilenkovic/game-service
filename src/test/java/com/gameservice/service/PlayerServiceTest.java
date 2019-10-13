package com.gameservice.service;

import static com.gameservice.service.exceptions.PlayerException.PlayerExceptionCode.DUPLICATED_NAME;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import com.gameservice.domain.Player;
import com.gameservice.domain.Team;
import com.gameservice.mapper.setups.PlayerSetup;
import com.gameservice.service.dto.PlayerSearchRequest;
import com.gameservice.service.dto.PlayerSearchResponse;
import com.gameservice.service.exceptions.PlayerException;
import com.gameservice.service.impl.PlayerServiceImpl;
import com.google.common.cache.CacheStats;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PlayerServiceTest extends BaseServiceTest {
	@Autowired
	private PlayerSetup playerSetup;

	@Autowired
	private PlayerService playerService;

	PlayerSearchResponse psr;

	@Test
	public void testSearch_PlayerName() {
		playerSetup.toplicanin1910();

		psr = playerService.searchPlayers(PlayerSearchRequest.builder().playerName("Marko").teamId("TOP/1910").build());
		assertThat(psr.getPlayers().get(0).getName(), equalTo("Marko Petrovic"));
	}

	@Test
	public void testSearch_PlayerNameAndPositions() {
		playerSetup.toplicanin1910();

		psr = playerService.searchPlayers(
				PlayerSearchRequest.builder().playerName("MARK").position("C").teamId("TOP/1910").build());
		assertThat(psr.getPlayers().get(0).getName(), equalTo("Marko Petrovic"));
	}

	@Test
	public void testSavePlayer() {
		log.info("Generate player setups...");
		List<Player> ps = playerSetup.toplicanin1910();
		Team t = ps.get(0).getTeam();
		Player p3 = Player.builder().team(t).name("Nikola Arnautovic").number("4").position("PG").playerId("n/arnaut3")
				.build();
		playerService.save(p3);
		assertThat(p3.getPosition(), is("PG"));

		p3.setPosition("PF");
		playerService.save(p3);
		Player p4 = playerService.getPlayer(p3.getId());
		PlayerAssert.assertPlayer(p3, "Nikola Arnautovic", "4", "PF", t.getTeamId());
		log.info("Search players");
		psr = playerService.searchPlayers(PlayerSearchRequest.builder().playerName("NIKO").position("PF")
				.teamId(p3.getTeam().getTeamId()).build());
		assertThat(psr.getPlayers().get(0), equalTo(p3));

	}

	@Test
	public void testPlayerSearch() {
		playerSetup.toplicanin1910();
		log.info("Search players");
		psr = playerService.searchPlayers(PlayerSearchRequest.builder().playerName("Jovan").teamId("TOP/1910").build());
		assertThat(psr.getPlayers().get(0).getName(), equalTo("Jovan Djokovic"));
	}

	@Rule
	public final ExpectedException thrown = ExpectedException.none();

	@Test
	public void testSavePlayer_Duplicated() {
		List<Player> ps = playerSetup.toplicanin1910();
		Team t = ps.get(0).getTeam();

		thrown.expect(PlayerException.class);
		thrown.expectMessage("[DUPLICATED_NAME] Name: Marko Petrovic is duplicated!");

		// custom matcher, more reusable
		thrown.expect(new ApplicationExceptionMatcher<>(PlayerException.class, DUPLICATED_NAME,
				"[DUPLICATED_NAME] Name: Marko Petrovic is duplicated!"));

		Player p1 = Player.builder().team(t).name("Marko Petrovic").team(t).number("15").position("C")
				.birthDate(LocalDateTime.now().minusYears(25)).college("University of Belgrade").experience(4)
				.nationality("SRB").playerId("m/petrovic").weight("115kg").height("219cm").build();
		playerService.save(p1);
	}

	@Test
	public void testGetTeamPlayers() {
		List<Player> ps = playerSetup.toplicanin1910();

		Map<Long, Player> map;

		Long teamId = ps.get(0).getTeam().getId();

		map = playerService.getTeamPlayers(teamId);
		assertThat(map.values(), containsInAnyOrder(ps.get(0), ps.get(1)));

		map = playerService.getTeamPlayers(teamId);
		assertNotNull(map);
	}

	@Test
	public void testGetPlayer_GuavaCached() throws InterruptedException {
		List<Player> ps = playerSetup.toplicanin1910();

		for (int i = 1; i <= 3; i++) {
			Player p1 = playerService.getPlayer(ps.get(0).getId());
			Player p2 = playerService.getPlayer(ps.get(1).getId());

			assertThat(p1, equalTo(ps.get(0)));
			assertThat(p2, equalTo(ps.get(1)));
		}

		CacheStats cs = ((PlayerServiceImpl) playerService).playerCacheStats();
		assertThat(cs.missCount(), equalTo(2L));
		assertThat(cs.hitCount(), equalTo(4L));
		assertThat(cs.loadSuccessCount(), equalTo(2L));

		Player px = ps.get(0);
		px.setName("Blah blah");
		playerService.save(px);

		Thread.sleep(5000);

		Player px2 = playerService.getPlayer(px.getId());
		assertThat(px2.getName(), equalTo(px.getName()));
	}

	@Autowired
	private CacheManager cacheManager;

	@Test
	public void testGetPlayer_SpringCached() {
		List<Player> ps = playerSetup.toplicanin1910();

		for (int i = 1; i <= 3; i++) {
			Player p1 = playerService.getPlayerCached(ps.get(0).getId());
			Player p2 = playerService.getPlayerCached(ps.get(1).getId());

			assertThat(p1, equalTo(ps.get(0)));
			assertThat(p2, equalTo(ps.get(1)));
		}

		Cache pc = cacheManager.getCache("players");
		// concurrent map does not have stats

		Player p2 = playerService.getPlayerCached(ps.get(1).getId());
		p2.setPosition("SF");
		playerService.save(p2);

		log.debug("P2 should get reloaded");
		p2 = playerService.getPlayerCached(ps.get(1).getId());
		playerService.save(p2);

		log.debug("P1 should remain");
		Player p1 = playerService.getPlayerCached(ps.get(0).getId());
		assertNotNull(p1);
	}

}
