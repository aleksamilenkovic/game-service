package com.gameservice.mapper;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.gameservice.GameServiceApplicationTests;
import com.gameservice.domain.Player;
import com.gameservice.domain.Season;
import com.gameservice.domain.Team;
import com.gameservice.parser.TeamParserBasketballRef;
import com.google.common.collect.Multimap;

import lombok.extern.slf4j.Slf4j;

@ActiveProfiles("test")
@Transactional
@Slf4j
public class TeamMapperTest extends GameServiceApplicationTests {
	@Autowired
	private TeamMapper teamMapper;

	private TeamParserBasketballRef teamParser = new TeamParserBasketballRef();

	@Autowired
	private PlayerMapper playerMapper;
	@Autowired
	private SeasonMapper seasonMapper;

	@Test
	public void returnAllTeamsAndPlayersFromSeason() {
		Season season = Season.builder().seasonYear(2016).teams(teamParser.readTeamsFromSeason(2016)).build();

		season.getTeams().forEach((team) -> {
			teamMapper.insert(team);
			team.getPlayers().forEach((player) -> playerMapper.insert(player));
		});
		assertEquals(season.getTeams().size(), 30);
		assertEquals(season.getTeams().get(4).getPlayers().size(), 18);
		List<Player> players = teamMapper.getPlayersForTeam("BOS/2016");
		Team tt = teamMapper.getWithPlayersByTeamId("BOS/2016");
		assertEquals(17, players.size());
	}

	@Test
	public void saveAllTeamsAndPlayersForAllSeasons() {

		Multimap<Integer, List<Team>> allTeams = teamParser.readTeamsFromSpecificSeasonTillNow(2010);
		for (List<Team> teams : allTeams.values()) {
			teams.forEach(team -> {
				teamMapper.insert(team);
				team.getPlayers().forEach(player -> playerMapper.insert(player));
			});
		}

		assertEquals(300, teamMapper.count());
	}

	@Test
	public void testCrud() throws Exception {

		assertEquals(teamMapper.count(), 873L);
		Season season = Season.builder().seasonYear(1910).build();
		seasonMapper.insert(season);
		LocalDateTime now = LocalDateTime.now();

		log.info("adding new team");
		Team t = Team.builder().name("KK Toplicanin").teamId("TOP/1910").seasonYear(1910).build();
		assertEquals(1, teamMapper.insert(t));

		log.info("Reloading a team");
		Team team = teamMapper.getByTeamId("TOP/1910");
		assertEquals("KK Toplicanin", team.getName());
		team.setName("Fafafa");

		log.info("Updating the team");
		assertEquals(teamMapper.update(team), 1);

		log.info("Again reloading updated team");
		Team ttt = teamMapper.getByTeamId("TOP/1910");
		assertEquals(ttt.getName(), "Fafafa");

		log.info("Deleting the team...");
		teamMapper.deleteById(team.getId());
		assertEquals(teamMapper.count(), 873L);

	}

	@Test
	public void testFetchingSeasonTeamsWithPlayers() {
		List<Team> seasonTeams = playerMapper.getSeasonTeamsAndPlayers(2019);
		log.info(seasonTeams.get(7).toString());
		assertEquals("Brooklyn Nets", seasonTeams.get(3).getName());
		assertEquals("DET/2019", seasonTeams.get(7).getTeamId());
		assertEquals("b/bullore01", seasonTeams.get(7).getPlayers().get(1).getPlayerId());
	}
}
