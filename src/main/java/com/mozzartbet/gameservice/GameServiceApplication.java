package com.mozzartbet.gameservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.Team;
import com.mozzartbet.gameservice.parser.MatchParser;
import com.mozzartbet.gameservice.parser.TeamParser;
import com.mozzartbet.gameservice.stats.MatchStats;

@SpringBootApplication
public class GameServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(GameServiceApplication.class, args);
    TeamParser teamParse = new TeamParser();
    MatchParser matchParse = new MatchParser();
    // teamParse.readPlayers("https://www.basketball-reference.com/teams/ORL/2019.html");

    // Team t =
    // teamParse.returnTeam("https://www.basketball-reference.com/teams/ORL/2017.html", "ORLANDO");
    // t.showTeam();
    // CITANJE SVIH SEZONA OD 1978
    // LinkedList<LinkedList<Team>> t =teamParse.readTeamsFromSpecificSeasonTillNow(1978);
    // teamParse.readTeamsFromSpecificSeasonTillNow(1999);
    // Match match = matchParse.returnMatch("201905160GSW");

    // Match match = matchParse

    /*
     * Match match = matchParse.returnMatch("201810160BOS",
     * "Portland Trail Blazers at Golden State Warriors Play-By-Play, May 16, 2019 _ Basketball-Reference.com"
     * ); System.out.println(match);
     */

    Match match = matchParse.returnMatch("201905160GSW",
        "Portland Trail Blazers at Golden State Warriors Play-By-Play, May 16, 2019 _ Basketball-Reference.com");
    Team team = teamParse.returnTeam("https://www.basketball-reference.com/teams/POR/2019.html",
        "Portland Trail Blazers");

    // PlayerStats ps = StatisticCaclulator.calculatePlayerStats(match, "k/kanteen01");
    // System.out.println(ps);

    // LinkedList<Team> teams = teamParse.readTeamsFromSeason(2001);

    // System.out.println(match);
    // LinkedList<Match> matches = matchParse.returnMatchesFromMonth(2001, "May");

    // Season s = matchParse.returnSeasonMatches(2001);
    // s.setTeams(teamParse.readTeamsFromSeason(2001));
    // System.out.println(s);
    MatchStats matchStats = new MatchStats();
    matchStats.calculateTeamStatsOnMatch(match, team);

    System.out.println(matchStats);
  }

}
