package com.mozzartbet.gameservice.domain;

import java.util.ArrayList;
import java.util.List;
import com.mozzartbet.gameservice.util.ConvertHelper;
import lombok.Data;

@Data
public class Match {

  private String matchId;
  private String date;
  private String awayTeam;
  private String finalScore;
  private String homeTeam;
  private List<Quarter> quarters;
  private int homeTeamPoints;
  private int awayTeamPoints;
  private List<String> homePlayersID;
  private List<String> awayPlayersID;

  // za pocetak nek timovi budu stringovi, kasnije klase timovi (a vec su napravljene metode za
  // vracanje timova)


  public Match() {
    finalScore = "0:0";
    // quarters = new Quarter[4];
    // overtimes = new LinkedList<Quarter>();
    homeTeam = "HomeTeam";
    awayTeam = "AwayTeam";
    homeTeamPoints = 0;
    awayTeamPoints = 0;
    quarters = new ArrayList<Quarter>();
    date = "";
  }

  public Match(String date, String awayTeam, String awayTeamPoints, String homeTeam,
      String homeTeamPoints, List<Quarter> quarters, String matchId) {
    this.date = date;
    this.homeTeam = homeTeam;
    this.awayTeam = awayTeam;
    this.homeTeamPoints = ConvertHelper.tryParseInt(homeTeamPoints);
    this.awayTeamPoints = ConvertHelper.tryParseInt(awayTeamPoints);
    this.quarters = quarters;
    this.finalScore = this.awayTeamPoints + " - " + this.homeTeamPoints;
    this.matchId = matchId;
    awayPlayersID = new ArrayList<String>();
    homePlayersID = new ArrayList<String>();
  }

  @Override
  public String toString() {
    return "Match [date=" + date + ", awayTeam=" + awayTeam + ", finalScore=" + finalScore
        + ", homeTeam=" + homeTeam + ", matchEvents=" + quarters + "]";
  }

}
