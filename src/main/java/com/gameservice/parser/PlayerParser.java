package com.gameservice.parser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gameservice.domain.Player;
import com.gameservice.domain.Team;
import com.gameservice.util.ConvertHelper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PlayerParser {
	public Player returnPlayer(Element row, Team team) {
		log.info(row.text());
		Elements cols = row.select("td");
		Element header = row.select("th").first();
		String id = ConvertHelper.returnPlayerId(cols.get(0).select("a").first().attr("href"));
		LocalDateTime date = ConvertHelper.convertStringToLocalDate(cols.get(4).attributes().get("csk"));
		int expirience;
		expirience = !cols.get(6).text().equals("R") ? Integer.parseInt(cols.get(6).text()) : 0;

		Player player = Player.builder().team(team).playerId(id).number(header.text()).name(cols.get(0).text())
				.position(cols.get(1).text()).height(cols.get(2).text()).weight(cols.get(3).text()).birthDate(date)
				.nationality(cols.get(5).text()).experience(expirience).college(cols.get(7).text()).build();
		return player;
	}

	public List<Player> returnPlayers(Elements rows, Team team) {
		List<Player> listOfPlayers = new ArrayList<Player>();
		for (int i = 1; i < rows.size(); i++)
			listOfPlayers.add(returnPlayer(rows.get(i), team));
		return listOfPlayers;
	}

}
