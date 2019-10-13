package com.gameservice.mapper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.gameservice.GameServiceApplicationTests;
import com.gameservice.domain.Match;
import com.gameservice.domain.Player;
import com.gameservice.domain.Season;
import com.gameservice.domain.Team;
import com.gameservice.domain.boxscore.PlayerStats;
import com.gameservice.parser.MatchParserBasketballRef;

import lombok.extern.slf4j.Slf4j;

@ActiveProfiles("test")
@Transactional
@Slf4j
public class PlayerStatsMapperTest extends GameServiceApplicationTests {
	@Autowired
	private PlayerMapper playerMapper;
	@Autowired
	private TeamMapper teamMapper;
	@Autowired
	private MatchMapper matchMapper;
	@Autowired
	private PlayerStatsMapper playerStatsMapper;
	@Autowired
	private SeasonMapper seasonMapper;
	private MatchParserBasketballRef matchParser = new MatchParserBasketballRef();

	@Test
	public void testAllPlayerStatsFromMatch() {
		Match match = matchParser.returnMatch("201905200POR", "pbp201905200POR");
		matchMapper.insert(match);
		match.getMatchStats().getAwayTeamPlayerStats().forEach(ps -> playerStatsMapper.insert(ps));
		match.getMatchStats().getHomeTeamPlayerStats().forEach(ps -> playerStatsMapper.insert(ps));

	}

	@Test
	public void testCrud() {
		log.info("Counting players stats...");
		assertEquals(0, playerStatsMapper.count());
		Season season = Season.builder().seasonYear(1910).build();
		seasonMapper.insert(season);
		Team team = Team.builder().name("KK Toplicanin").seasonYear(1910).teamId("TOP/2019").build();
		Player player = Player.builder().name("Marko Petrovic").experience(1).team(team).number("1").build();
		Match match = Match.builder().awayTeam(team).seasonYear(1910).finalScore("112-110").matchId("2140/12").build();
		teamMapper.insert(team);
		playerMapper.insert(player);
		matchMapper.insert(match);
		PlayerStats ps = PlayerStats.builder().assists(5).blocks(7).points(12).fieldGoalAttempts(21)
				.threePointFGAttempts(5).threePointFG(2).freeThrowAttempts(12).player(player).team(team).match(match)
				.build();
		assertEquals(1, playerStatsMapper.insert(ps));
		PlayerStats playerStats = playerStatsMapper.getById(ps.getId());
		assertEquals(7, playerStats.getBlocks());

		PlayerStats playerStatsByMatchAndPlayerId = playerStatsMapper.getByMatchIdAndPlayerId(player.getPlayerId(),
				match.getMatchId());
		assertEquals(7, playerStatsByMatchAndPlayerId.getBlocks());

		playerStatsMapper.deleteById(playerStats.getId());
		assertEquals(playerStatsMapper.count(), 0);
	}
}
