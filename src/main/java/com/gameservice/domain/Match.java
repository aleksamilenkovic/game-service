package com.gameservice.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.gameservice.domain.boxscore.MatchStats;
import com.gameservice.util.ConvertHelper;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Match implements BaseEntity {
	@EqualsAndHashCode.Include
	private Long id;

	private LocalDateTime createdOn;
	private LocalDateTime modifiedOn;
	private String matchId;
	private LocalDateTime dateTime;
	private Team awayTeam;
	private String finalScore;
	private Team homeTeam;
	private List<Quarter> quarters;
	private int homeTeamPoints;
	private int awayTeamPoints;
	private List<String> homePlayersID;
	private List<String> awayPlayersID;
	private MatchStats matchStats;
	private int seasonYear;
	// za pocetak nek timovi budu stringovi, kasnije klase timovi (a vec su
	// napravljene metode za
	// vracanje timova)

	public Match(LocalDateTime date, Team awayTeam, String awayTeamPoints, Team homeTeam, String homeTeamPoints,
			List<Quarter> quarters, String matchId, int year) {
		this.dateTime = date;
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.homeTeamPoints = ConvertHelper.tryParseInt(homeTeamPoints);
		this.awayTeamPoints = ConvertHelper.tryParseInt(awayTeamPoints);
		this.quarters = quarters;
		this.finalScore = this.awayTeamPoints + " - " + this.homeTeamPoints;
		this.matchId = matchId;
		awayPlayersID = new ArrayList<String>();
		homePlayersID = new ArrayList<String>();
		seasonYear = year;
		matchStats = new MatchStats();
	}

	@Override
	public String toString() {
		return "Match [date=" + dateTime + ", awayTeam=" + awayTeam + ", finalScore=" + finalScore + ", homeTeam="
				+ homeTeam + ", matchEvents=" + quarters + "]";
	}

	public void calculateStats() {
		matchStats.calculateMatchStats(this);
	}
}
