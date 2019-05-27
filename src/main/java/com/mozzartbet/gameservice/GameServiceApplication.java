package com.mozzartbet.gameservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.mozzartbet.gameservice.domain.Match;
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
    Match match = matchParse.returnMatch("201905160GSW");

    // Match match = matchParse

    // matchParse.returnMatch("201810160BOS");
    // LinkedList<Team> teams = teamParse.readTeamsFromSeason(2001);

    // System.out.println(match);
    // LinkedList<Match> matches = matchParse.returnMatchesFromMonth(2001, "May");

    // Season s = matchParse.returnSeasonMatches(2001);

  }

}
