package com.mozzartbet.gameservice.parser;


import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.domain.Team;
import com.mozzartbet.gameservice.exception.UrlException;
import com.mozzartbet.gameservice.util.ConvertHelper;
import com.mozzartbet.gameservice.util.JsoupHelper;



public class TeamParser {


  public void readPlayers(String pageUrl) {
    // first method for exersice jsoup-
    Document doc;
    try {
      doc = JsoupHelper.connectToLivePage(pageUrl);
    } catch (UrlException e) {
      System.out.println(e);
      return;
    }


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

  public List<Player> returnPlayers(Elements rows, String teamId) {
    List<Player> listOfPlayers = new ArrayList<Player>();

    for (int i = 1; i < rows.size(); i++) {
      Elements cols = rows.get(i).select("td");
      Element header = rows.get(i).select("th").first();
      String id = ConvertHelper.returnPlayerId(cols.get(0).select("a").first().attr("abs:href"));
      int expirience;
      expirience = !cols.get(6).text().equals("R") ? Integer.parseInt(cols.get(6).text()) : 0;
      Player player = new Player(id, teamId, header.text(), cols.get(0).text(), cols.get(1).text(),
          cols.get(2).text(), cols.get(3).text(), cols.get(4).text(), cols.get(5).text(),
          expirience, cols.get(7).text());
      listOfPlayers.add(player);
    }
    return listOfPlayers;
  }

  public Team returnTeam(String pageUrl, String teamName) {
    Team t = new Team();
    t.setTeamName(teamName);
    Document doc;
    try {
      doc = JsoupHelper.connectToLivePage(pageUrl);
    } catch (UrlException e) {
      System.out.println(e);
      return null;
    } // POR/2019.html
    String teamId = pageUrl.substring(pageUrl.length() - 13, pageUrl.length() - 5);
    Elements rows = doc.select("table#roster tr");
    t.setPlayers(returnPlayers(rows, teamId));
    return t;
  }


  public List<Team> readTeamsFromSeason(int seasonYear) {
    String url = "https://www.basketball-reference.com/leagues/NBA_" + seasonYear + ".html";
    List<Team> teams = new ArrayList<Team>();
    Document doc;
    try {
      doc = JsoupHelper.connectToLivePage(url);
    } catch (UrlException e) {
      System.out.println(e);
      return null;
    }
    Elements rows = seasonYear > 1970 ? doc.select("table#divs_standings_E tr.full_table")
        : doc.select("table#divs_standings_ tr.full_table");
    /*
     * if (seasonYear > 1970) rows = doc.select("table#divs_standings_E tr.full_table"); else rows =
     * doc.select("table#divs_standings_ tr.full_table");
     */
    for (int j = 0; j < 2; j++) {
      for (int i = 0; i < rows.size(); i++) {
        // dobijam sada sve redove jedne konferencije
        Elements columns = rows.get(i).select("th");
        Element firstColumn = columns.get(0).select("a").first();
        String link = firstColumn.attr("abs:href");

        Team t = returnTeam(link, firstColumn.text());
        // t.showTeam();
        teams.add(t);
      }
      if (seasonYear <= 1970)
        break;
      rows = doc.select("table#divs_standings_W tr.full_table");
    }
    return teams;
  }

  public Multimap<Integer, List<Team>> readTeamsFromSpecificSeasonTillNow(int year) {
    Multimap<Integer, List<Team>> allTeams = ArrayListMultimap.create();
    do {
      System.out
          .println("**********************SEASON YEAR " + year + "****************************** ");
      allTeams.put(year, readTeamsFromSeason(year));
    } while (year++ < 2019);
    return allTeams;
  }

}
