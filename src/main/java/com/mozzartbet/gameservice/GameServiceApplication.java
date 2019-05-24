package com.mozzartbet.gameservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.mozzartbet.gameservice.parser.JSoupMatchParser;
import com.mozzartbet.gameservice.parser.JSoupTeamParser;

@SpringBootApplication
public class GameServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(GameServiceApplication.class, args);
    JSoupTeamParser teamParse = new JSoupTeamParser();
    JSoupMatchParser matchParse = new JSoupMatchParser();
    // teamParse.readPlayers("https://www.basketball-reference.com/teams/ORL/2019.html");

    // Team t =
    // teamParse.returnTeam("https://www.basketball-reference.com/teams/ORL/2017.html", "ORLANDO");
    // t.showTeam();
    // CITANJE SVIH SEZONA OD 1978
    // LinkedList<LinkedList<Team>> t =teamParse.readTeamsFromSpecificSeasonTillNow(1978);
    // teamParse.readTeamsFromSpecificSeasonTillNow(1999);
    // matchParse.returnMatch("https://www.basketball-reference.com/boxscores/pbp/201905160GSW.html");

    // Match match = matchParse
    // matchParse.returnMatchById("201810160BOS");
    // System.out.println(match);
    // LinkedList<Match> matches = matchParse.returnMatchesFromMonth(
    // "https://www.basketball-reference.com/leagues/NBA_2019_games-april.html");
    // Season s = matchParse.returnSeasonMatches(2019);
  }

}
