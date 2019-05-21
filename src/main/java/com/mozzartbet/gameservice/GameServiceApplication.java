package com.mozzartbet.gameservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.mozzartbet.gameservice.parser.JSoupParser;

@SpringBootApplication
public class GameServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GameServiceApplication.class, args);
		JSoupParser pars = new JSoupParser();
		//pars.readPlayers("https://www.basketball-reference.com/teams/MIL/2019.html");
		pars.returnListOfPlayers("https://www.basketball-reference.com/teams/TOR/2019.html");
		
	}

}
