package com.gameservice.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.gameservice.domain.Match;
import com.gameservice.domain.boxscore.PlayerStats;
import com.gameservice.stats.MatchStatsCalculator;
import com.gameservice.stats.StatisticCaclulator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StatsTests {

	@Test
	public void testPlayerStats() {
		MatchParserBasketballRef matchParse = new MatchParserBasketballRef();
		// TeamParser teamParse = new TeamParser();
		Match match = matchParse.returnMatch("201905160GSW", "pbp201905200POR");
		/*
		 * Team team = teamParse.returnTeam(
		 * "https://www.basketball-reference.com/teams/POR/2019.html",
		 * "Portland Trail Blazers"); Team team2 = teamParse.returnTeam(
		 * "https://www.basketball-reference.com/teams/GSW/2019.html",
		 * "Golden State Warriors");
		 */
		// team.getPlayers().addAll(team2.getPlayers());
		StatisticCaclulator scalc = new StatisticCaclulator();
		Map<String, PlayerStats> playersStats = scalc.returnPlayersStatsIndividual(match, match.getHomePlayersID(),
				match.getAwayPlayersID());
		assertEquals(playersStats.get("c/curryst01").getPoints(), 37);
		log.info(playersStats.toString());
	}

	@Test
	public void testTeamStats() {
		// TEST DA LI JE LILARD NA MECU DAO 23 POENA
		MatchParserBasketballRef matchParse = new MatchParserBasketballRef();
		// TeamParser teamParse = new TeamParser();
		Match match = matchParse.returnMatch("201905160GSW", "pbp201905200POR");
		/*
		 * Team team = teamParse.returnTeam(
		 * "https://www.basketball-reference.com/teams/POR/2019.html",
		 * "Portland Trail Blazers"); Team team2 = teamParse.returnTeam(
		 * "https://www.basketball-reference.com/teams/GSW/2019.html",
		 * "Golden State Warriors");
		 */
		// team.getPlayers().addAll(team2.getPlayers());
		StatisticCaclulator scalc = new StatisticCaclulator();
		Map<String, PlayerStats> playersStats = (HashMap<String, PlayerStats>) scalc.returnPlayersStatsIndividual(match,
				match.getHomePlayersID(), match.getAwayPlayersID());
		/*
		 * for (PlayerStats ps : playersStats) { System.out.println(ps); }
		 */
		System.out.println(playersStats);
		// da li je broj
		assertFalse(playersStats == null);
		// assertEquals(playersStats.get(0).getPoints(), 23);
	}

	@Test
	public void testMatchStats() {
		MatchParserBasketballRef matchParse = new MatchParserBasketballRef();
		// +TeamParser teamParse = new TeamParser();
		Match match = matchParse.returnMatch("201906100TOR", null);
		/*
		 * Team team1 = teamParse.returnTeam(
		 * "https://www.basketball-reference.com/teams/POR/2019.html",
		 * "Portland Trail Blazers"); Team team2 = teamParse.returnTeam(
		 * "https://www.basketball-reference.com/teams/GSW/2019.html",
		 * "Golden State Warriors");
		 */
		MatchStatsCalculator stats = new MatchStatsCalculator();
		stats.calculateMatchStats(match, "");

		assertEquals(stats.getHomeTeamStatsSummary().getThreePointFG(), 8);

	}

}
