package com.mozzartbet.gameservice.parser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.domain.Team;
import com.mozzartbet.gameservice.util.ConvertHelper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PlayerParser {
  public Player returnPlayer(Element row, String teamId) {
    log.info(row.text());
    Elements cols = row.select("td");
    Element header = row.select("th").first();
    String id = ConvertHelper.returnPlayerId(cols.get(0).select("a").first().attr("abs:href"));
    LocalDateTime date =
        ConvertHelper.convertStringToLocalDate(cols.get(4).attributes().get("csk"));
    int expirience;
    expirience = !cols.get(6).text().equals("R") ? Integer.parseInt(cols.get(6).text()) : 0;

    Player player = Player.builder().team(Team.builder().teamId(teamId).build()).playerId(id)
        .number(header.text()).name(cols.get(0).text()).position(cols.get(1).text())
        .height(cols.get(2).text()).weight(cols.get(3).text()).birthDate(date)
        .nationality(cols.get(5).text()).experience(expirience).college(cols.get(7).text()).build();
    return player;
  }

  public List<Player> returnPlayers(Elements rows, String teamId) {
    List<Player> listOfPlayers = new ArrayList<Player>();
    for (int i = 1; i < rows.size(); i++)
      listOfPlayers.add(returnPlayer(rows.get(i), teamId));
    return listOfPlayers;
  }

}
