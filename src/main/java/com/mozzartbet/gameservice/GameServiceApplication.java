package com.mozzartbet.gameservice;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GameServiceApplication implements ApplicationRunner {

  public static void main(String[] args) {
    SpringApplication.run(GameServiceApplication.class, args);

  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    // TeamParser teamParse = new TeamParser();
    // MatchParser matchParse = new MatchParser();
    // teamParse.readPlayers("https://www.basketball-reference.com/teams/ORL/2019.html");

    // Match match = matchParse.returnMatch("201905300TOR", null);// ,
    // "Portland Trail Blazers at Golden State Warriors Play-By-Play, May 16, 2019 _
    // Basketball-Reference.com");
    /*
     * Team team1 = teamParse.returnTeam("https://www.basketball-reference.com/teams/TOR/2019.html",
     * "Toronto Raptors"); Team team2 =
     * teamParse.returnTeam("https://www.basketball-reference.com/teams/GSW/2019.html",
     * "Golden State Warriors");
     */

    // Season s = matchParse.returnSeasonMatches(2001);
    // s.setTeams(teamParse.readTeamsFromSeason(2001));
    // System.out.println(s);
    // MatchStats matchStats = new MatchStats();
    // matchStats.calculateMatchStats(match, "rebound");
    /*
     * System.out.println(matchStats.getBestPlayersStats() + "\n");
     * System.out.println("All players stats\n" + matchStats.getPlayersStats() + "\n");
     * System.out.println("Line score: \n" + matchStats.getLineScore());
     */

  }

}
