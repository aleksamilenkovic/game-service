package com.gameservice.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gameservice.domain.Season;
import com.gameservice.domain.Team;
import com.gameservice.mapper.setups.PlayerSetup;
import com.gameservice.repository.SeasonRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TeamServiceTest extends BaseServiceTest {
	@Autowired
	private TeamService teamService;
	@Autowired
	private SeasonRepository seasonRepo;
	@Autowired
	private PlayerSetup playerSetup;

	@Test
	public void testTeamWithPlayers() {
		playerSetup.toplicanin1910();
		// get team with players;
		Team team = teamService.getTeamByTeamId("TOP/1910");
		assertEquals("KK Toplicanin", team.getName());
		assertEquals(2, team.getPlayers().size());
	}

	@Test
	public void testTeamCache() {
		Season season = Season.builder().seasonYear(1923).build();
		seasonRepo.insert(season);

		Team team1 = Team.builder().name("KK Mozzart").seasonYear(1923).teamId("mozzart/1923").build();
		Team team2 = Team.builder().name("KK Toplicanin").seasonYear(1923).teamId("TOP/1923").build();

		teamService.save(team1);
		teamService.save(team2);

		Team teamFromDB1 = teamService.getTeam(team1.getId());
		Team teamFromDB2 = teamService.getTeam(team2.getId());

		assertThat(team1, is(teamFromDB1));
		assertThat(team2, is(teamFromDB2));
	}

	// SAME METHOD TO SEE HOW CACHE SPEEDS UP
	@Test
	public void testTeamCache1() {
		Season season = Season.builder().seasonYear(1923).build();
		seasonRepo.insert(season);

		Team team1 = Team.builder().name("KK Mozzart").seasonYear(1923).teamId("mozzart/1923").build();
		Team team2 = Team.builder().name("KK Toplicanin").seasonYear(1923).teamId("TOP/1923").build();

		teamService.save(team1);
		teamService.save(team2);

		Team teamFromDB1 = teamService.getTeam(team1.getId());
		Team teamFromDB2 = teamService.getTeam(team2.getId());

		assertThat(team1, is(teamFromDB1));
		assertThat(team2, is(teamFromDB2));
	}

}
