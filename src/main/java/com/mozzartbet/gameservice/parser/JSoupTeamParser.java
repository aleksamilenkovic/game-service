package com.mozzartbet.gameservice.parser;


import java.util.LinkedList;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.domain.Team;
import com.mozzartbet.gameservice.util.ConvertHelper;
import com.mozzartbet.gameservice.util.JsoupHelper;



public class JSoupTeamParser {


  public void readPlayers(String pageUrl) {
    // first method for exersice jsoup-
    Document doc = JsoupHelper.connectToLivePage(pageUrl);
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

  public LinkedList<Player> returnPlayers(Elements rows) {
    LinkedList<Player> listOfPlayers = new LinkedList<Player>();

    rows.remove(0);
    for (int i = 0; i < rows.size(); i++) {
      Elements cols = rows.get(i).select("td");
      Element header = rows.get(i).select("th").first();
      String id = ConvertHelper.returnPlayerId(cols.get(0).select("a").first().attr("abs:href"));
      Player player = new Player(header.text(), cols.get(0).text(), cols.get(1).text(),
          cols.get(2).text(), cols.get(3).text(), cols.get(4).text(), cols.get(5).text(),
          cols.get(6).text(), cols.get(7).text(), id);
      listOfPlayers.add(player);
    }

    return listOfPlayers;
  }

  public Team returnTeam(String pageUrl, String teamName) {
    Team t = new Team();
    t.setTeamName(teamName);
    Document doc = JsoupHelper.connectToLivePage(pageUrl);
    if (doc == null)
      return null;

    Elements rows = doc.select("table#roster tr");
    t.setPlayers(returnPlayers(rows));
    return t;
  }


  public LinkedList<Team> readTeamsFromSeason(int seasonYear) {
    String url = "https://www.basketball-reference.com/leagues/NBA_" + seasonYear + ".html";
    LinkedList<Team> teams = new LinkedList<Team>();
    Document doc = JsoupHelper.connectToLivePage(url);
    Elements rows = null;
    if (doc == null)
      return null;

    if (seasonYear > 1970)
      rows = doc.select("table#divs_standings_E tr.full_table");
    else
      rows = doc.select("table#divs_standings_ tr.full_table");
    for (int j = 0; j < 2; j++) {
      for (int i = 0; i < rows.size(); i++) {
        // dobijam sada sve redove jedne konferencije
        Elements columns = rows.get(i).select("th");
        Element firstColumn = columns.get(0).select("a").first();
        String link = firstColumn.attr("abs:href");

        Team t = returnTeam(link, firstColumn.text());
        t.showTeam();
        teams.add(t);
      }
      if (seasonYear <= 1970)
        break;
      rows = doc.select("table#divs_standings_W tr.full_table");
    }


    return teams;
  }

  public LinkedList<LinkedList<Team>> readTeamsFromSpecificSeasonTillNow(int year) {
    LinkedList<LinkedList<Team>> allTeams = new LinkedList<LinkedList<Team>>();
    do {
      System.out
          .println("**********************SEASON YEAR " + year + "****************************** ");
      allTeams.add(readTeamsFromSeason(year));
    } while (year++ < 2019);
    return allTeams;
  }

}
