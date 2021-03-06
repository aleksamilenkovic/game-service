package com.gameservice.domain.boxscore;

import java.util.List;

import com.gameservice.domain.Match;
import com.gameservice.stats.MatchStatsCalculator;
import com.google.common.collect.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MatchStats {
	private Match match;
	private Table<String, String, Integer> lineScore;
	private List<PlayerStats> homeTeamPlayerStats;
	private List<PlayerStats> awayTeamPlayerStats;

	public void calculateMatchStats(Match match) {
		this.match = match;
		MatchStatsCalculator msc = new MatchStatsCalculator();
		msc.calculateMatchStats(match, null);
		lineScore = msc.getLineScore();
		homeTeamPlayerStats = msc.getHomeTeamPlayerStats();
		awayTeamPlayerStats = msc.getAwayTeamPlayerStats();
	}
}
