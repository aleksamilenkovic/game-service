package com.mozzartbet.gameservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.mozzartbet.gameservice.parser.JSoupTeamParser;

@SpringBootApplication
public class GameServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(GameServiceApplication.class, args);
    JSoupTeamParser pars = new JSoupTeamParser();
    // pars.readPlayers("https://www.basketball-reference.com/teams/ORL/2019.html");

    // pars.readMatch("https://www.basketball-reference.com/boxscores/pbp/201905160GSW.html");
    // Team t = pars.returnTeam("https://www.basketball-reference.com/teams/ORL/2017.html",
    // "ORLANDO");
    // t.showTeam();
    // CITANJE SVIH SEZONA OD 1978
    // LinkedList<LinkedList<Team>> t =pars.readTeamsFromSpecificSeasonTillNow(1978);
    pars.readTeamsFromSpecificSeasonTillNow(1999);

  }

}
