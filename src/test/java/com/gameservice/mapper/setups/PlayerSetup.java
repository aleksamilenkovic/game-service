package com.gameservice.mapper.setups;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gameservice.domain.Player;
import com.gameservice.domain.Season;
import com.gameservice.domain.Team;
import com.gameservice.mapper.PlayerMapper;
import com.gameservice.mapper.SeasonMapper;
import com.gameservice.mapper.TeamMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PlayerSetup {
	@Autowired
	private TeamMapper teamMapper;

	@Autowired
	private PlayerMapper playerMapper;

	@Autowired
	private SeasonMapper seasonMapper;

	public List<Player> toplicanin1910() {
		Team t = teamToplicanin1910();

		log.info("Adding players");
		Player p1 = Player.builder().team(t).name("Marko Petrovic").team(t).number("15").position("C")
				.birthDate(LocalDateTime.now().minusYears(25)).college("University of Belgrade").experience(4)
				.nationality("SRB").playerId("m/petrovic").weight("115kg").height("219cm").build();
		playerMapper.insert(p1);

		Player p2 = Player.builder().team(t).name("Jovan Djokovic").team(t).number("9").position("PG")
				.birthDate(LocalDateTime.now().minusYears(23)).college("University of Belgrade").experience(2)
				.nationality("SRB").playerId("j/djokovic1").weight("101kg").height("202cm").build();
		playerMapper.insert(p2);

		log.info("Fetching players for team");
		List<Player> ps = playerMapper.getPlayersForTeam(t.getId());
		return ps;
	}

	public Team teamToplicanin1910() {
		log.info("Adding a new season");
		Season s = Season.builder().seasonYear(1910).build();
		seasonMapper.insert(s);
		log.info("Adding a new team");
		Team t = Team.builder().name("KK Toplicanin").teamId("TOP/1910").seasonYear(1910).build();
		teamMapper.insert(t);
		return t;
	}
}
