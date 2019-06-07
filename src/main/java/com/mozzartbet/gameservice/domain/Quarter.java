package com.mozzartbet.gameservice.domain;

import java.util.List;
import com.mozzartbet.gameservice.util.ConvertHelper;
import lombok.Data;

@Data
public class Quarter {
  public Quarter(String quarter, List<MatchEvent> quarterEvents) {
    this.quarterName = quarter;
    this.matchEvents = quarterEvents;
    // Ovo sam stavio zato sto se desava bug na jednoj stranici da se zavrsi cetvrtina pa onda
    // bude rebound sto je nemoguce na
    // https://www.basketball-reference.com/boxscores/pbp/201112260PHO.html
    String result[] = quarterEvents.get(quarterEvents.size() - 2).getScoreSummary().isEmpty()
        ? quarterEvents.get(quarterEvents.size() - 1).getScoreSummary().split("-")
        : quarterEvents.get(quarterEvents.size() - 2).getScoreSummary().split("-");
    this.pointsAwayTeam = ConvertHelper.tryParseInt(result[0]);
    this.pointsHomeTeam = result.length == 2 ? ConvertHelper.tryParseInt(result[1]) : 0;
  }

  private int pointsHomeTeam = 0;
  private int pointsAwayTeam = 0;
  private List<MatchEvent> matchEvents;
  private String quarterName;
}
