package com.mozzartbet.gameservice.parser;


import java.util.LinkedList;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.domain.Team;
import com.mozzartbet.gameservice.util.JsoupHelper;



public class JSoupParser {


  public void readPlayers(String pageUrl) {
    Document doc = JsoupHelper.connectToPage(pageUrl);
    if (doc == null)
      return;

    Elements rows = doc.select("table#roster tr");
    System.out.println("\n\n\nPlayers:   Position:  Height:  W:  Birth Date:  E:  College:  ");
    System.out.println("***********************************************************************");
    for (int i = 0; i < rows.size(); i++) {

      Elements cols = rows.get(i).select("td");
      for (int j = 0; j < cols.size(); j++) {
        if (j == 5)
          continue;
        System.out.print(cols.get(j).text() + " ");

      }
      System.out.println("\n-------------------------------------------------------------------");
    }

  }

  public LinkedList<Player> returnPlayers(String pageUrl) {
    LinkedList<Player> listOfPlayers = new LinkedList<Player>();
    Document doc = JsoupHelper.connectToPage(pageUrl);
    if (doc == null)
      return null;

    Elements rows = doc.select("table#roster tr");
    rows.remove(0);
    Elements th = rows.select("th");
    for (int i = 0; i < rows.size(); i++) {
      Elements cols = rows.get(i).select("td");
      Player player = new Player();
      // System.out.println(cols.get(0).text());
      player.setNumber(Integer.parseInt(th.get(i).text()));
      player.setName(cols.get(0).text());
      player.setPosition(cols.get(1).text());
      player.setHeight(cols.get(2).text());
      player.setWeight(cols.get(3).text());
      player.setBirthDate(cols.get(4).text());
      player.setNationality(cols.get(5).text());
      if (!cols.get(6).text().equals("R"))
        player.setExpirience(Integer.parseInt(cols.get(6).text()));
      else
        player.setExpirience(0);
      if (!cols.get(7).text().isEmpty())
        player.setCollege(cols.get(7).text());
      else
        player.setCollege("unkown");
      listOfPlayers.add(player);
      // System.out.println(player);
    }

    return listOfPlayers;
  }



  public void readMatch(String pageUrl) {
    // Exercise with JSOUP
    Document doc = JsoupHelper.connectToPage(pageUrl);
    if (doc == null)
      return;

    Elements rows = doc.select("table#pbp tr");
    for (int i = 0; i < rows.size(); i++) {
      Elements cols = rows.get(i).select("td");
      for (int j = 0; j < cols.size(); j++)
        System.out.print(cols.get(j).text() + " ");
      System.out.println("");
    }
  }

  public Team readTeamFromSeason(String seasonYear, String teamName) {
    String url = "https://www.basketball-reference.com/leagues/NBA_" + seasonYear + ".html";
    LinkedList<Player> listOfPlayers = new LinkedList<Player>();
    Document doc = JsoupHelper.connectToPage(url);
    if (doc == null)
      return null;

    Elements rows = doc.select("table#divs_standings_E tr.full_table");
    for (int i = 0; i < rows.size(); i++) {
      // dobijam sada sve redove istocne konferencije
      // nastavak sutra 23.05. ovde
    }
    System.out.println(rows.text());

    return null;
  }


}
