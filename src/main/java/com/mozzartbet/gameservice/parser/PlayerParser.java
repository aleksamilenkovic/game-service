package com.mozzartbet.gameservice.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.util.ConvertHelper;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class PlayerParser {
	public Player returnPlayer(Element row,String teamId) {
	  log.info(row.text());
		Elements cols = row.select("td");
	      Element header = row.select("th").first();
	      String id = ConvertHelper.returnPlayerId(cols.get(0).select("a").first().attr("abs:href"));
	      int expirience;
	      expirience = !cols.get(6).text().equals("R") ? Integer.parseInt(cols.get(6).text()) : 0;
	      return new Player(id, teamId, header.text(), cols.get(0).text(), cols.get(1).text(),
	          cols.get(2).text(), cols.get(3).text(), cols.get(4).text(), cols.get(5).text(),
	          expirience, cols.get(7).text());
	}
	public List<Player> returnPlayers(Elements rows, String teamId) {
	    List<Player> listOfPlayers = new ArrayList<Player>();
	    for (int i = 1; i < rows.size(); i++) 
	      listOfPlayers.add(returnPlayer(rows.get(i),teamId));
	    return listOfPlayers;
	  }

}
