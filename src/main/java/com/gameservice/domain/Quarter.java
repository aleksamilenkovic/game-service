package com.gameservice.domain;

import java.time.LocalDateTime;
import java.util.List;

import com.gameservice.util.ConvertHelper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Quarter implements BaseEntity {
	@EqualsAndHashCode.Include
	private Long id;

	private LocalDateTime createdOn;
	private LocalDateTime modifiedOn;
	private int pointsHomeTeam = 0;
	private int pointsAwayTeam = 0;
	private List<MatchEvent> matchEvents;
	private Match match;
	private String name;
	private LocalDateTime quarterTime;

	public Quarter(String quarter, List<MatchEvent> quarterEvents, Match match) {
		this.name = quarter;
		this.matchEvents = quarterEvents;
		// Ovo sam stavio zato sto se desava bug na jednoj stranici da se zavrsi
		// cetvrtina pa onda
		// bude rebound sto je nemoguce na
		// https://www.basketball-reference.com/boxscores/pbp/201112260PHO.html
		this.match = match;
		quarterTime = name.equals("1st Q") ? match.getDateTime()
				: match.getQuarters().get(match.getQuarters().size() - 1).getQuarterTime();
		quarterTime = name.equals("1st Q") ? quarterTime
				: match.getQuarters().get(match.getQuarters().size() - 1).getName().contains("OT")
						? quarterTime.plusMinutes(10)
						: quarterTime.plusMinutes(17);
	}

	public void calculateResult() {
		if (matchEvents.size() < 2)
			return;
		String result[] = matchEvents.get(matchEvents.size() - 2).getScoreSummary().isEmpty()
				? matchEvents.get(matchEvents.size() - 1).getScoreSummary().split("-")
				: matchEvents.get(matchEvents.size() - 2).getScoreSummary().split("-");
		this.pointsAwayTeam = ConvertHelper.tryParseInt(result[0]);
		this.pointsHomeTeam = result.length == 2 ? ConvertHelper.tryParseInt(result[1]) : 0;
	}
}
