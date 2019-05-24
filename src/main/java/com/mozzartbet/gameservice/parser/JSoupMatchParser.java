package com.mozzartbet.gameservice.parser;

import java.util.LinkedList;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.MatchEvent;
import com.mozzartbet.gameservice.domain.MatchEventType;
import com.mozzartbet.gameservice.domain.Season;
import com.mozzartbet.gameservice.util.JsoupHelper;

public class JSoupMatchParser {
  int quarter = 1;

  public MatchEvent returnMatchEvent(Element row) {
    MatchEvent matchEvent = null;
    Elements cols = row.select("td");
    String timestamp = cols.get(0).text();
    String firstAction = cols.get(1).text();
    String score;
    MatchEventType actionType;
    if (firstAction.contains("Jump ball") || firstAction.contains("quarter")
        || firstAction.contains("overtime")) {
      if (firstAction.contains("End of")) // AKO JE KRAJ CETVRTINE
        quarter++;
      actionType = MatchEventType.NEUTRALEVENT;
      matchEvent = new MatchEvent(actionType, firstAction, timestamp, quarter);
      return matchEvent;
    }
    // PROVERA DA LI JE NEUTRALNI DOGADJAJ SKOK ZA LOPTU/ POCETAK ILI KRAJ CETVRTINE
    else if (firstAction.isEmpty()) {
      String secondAction = cols.get(5).text();
      String pointsMadeHomeTeam = cols.get(4).text();
      score = cols.get(3).text();
      actionType = MatchEventType.SCOREHOMETEAM;
      matchEvent =
          new MatchEvent(actionType, secondAction, pointsMadeHomeTeam, timestamp, quarter, score);
      return matchEvent;
    } else {
      String pointsMadeAwayTeam = cols.get(2).text();
      score = cols.get(3).text();
      actionType = MatchEventType.SCOREAWAYTEAM;
      matchEvent =
          new MatchEvent(actionType, firstAction, pointsMadeAwayTeam, timestamp, quarter, score);
    }

    // UBACITI PROMENU tj. UPDATE AKO JE NEKO DAO KOS DA SE PRIKAZE KOLIKO POENA JE DAO
    // (cols.get(2))


    return matchEvent;
  }

  public LinkedList<MatchEvent> returnMatchEvents(String pageUrl) {
    // Match match = new Match();
    LinkedList<MatchEvent> matchEvents = new LinkedList<MatchEvent>();
    Document doc = JsoupHelper.connectToLivePage(pageUrl);
    if (doc == null)
      return matchEvents;

    Elements rows = doc.select("table#pbp tr");
    for (int i = 0; i < rows.size(); i++) {
      if (rows.get(i).select("td").text().isEmpty())
        continue;
      MatchEvent matchEvent = returnMatchEvent(rows.get(i));
      matchEvents.add(matchEvent);
      // ispisi red
      System.out.println(rows.get(i).text());

      // OVDE SMO UCITALI CELU LINIJU tj. MATCH EVENT
      // prva kolona timestamp, action
      // Elements cols = rows.get(i).select("td");


      // System.out.println("");
    }
    // setMatchEvents(matchEvents);
    return matchEvents;
  }

  public Match returnMatchById(String id) {
    Match match = null;
    String url = "https://www.basketball-reference.com/boxscores/pbp/" + id + ".html";
    Document doc = JsoupHelper.connectToLivePage(url);
    if (doc == null)
      return match;
    Element scorebox = doc.getElementsByClass("scorebox").first();
    String awayTeam = scorebox.select("strong").get(0).text();
    String homeTeam = scorebox.select("strong").get(1).text();
    String[] dt = doc.select("div.scorebox_meta").select("div").first().text().split(" ");
    String date = dt[2] + dt[3] + dt[4];
    String pointsAwayTeam = scorebox.select("div.score").select("div").get(0).text();
    String pointsHomeTeam = scorebox.select("div.score").get(1).text();
    LinkedList<MatchEvent> matchEvents = returnMatchEvents(url);
    match = new Match(date, awayTeam, pointsAwayTeam, homeTeam, pointsHomeTeam, matchEvents);
    return match;
  }

  public Match returnMatch(Element row, Elements cols) {
    Match match = null;
    String date = row.select("th").first().text();
    String awayTeam = cols.get(1).text();
    String pointsAwayTeam = cols.get(2).text();
    String homeTeam = cols.get(3).text();
    String pointsHomeTeam = cols.get(4).text();
    String pbpURL = cols.get(5).select("a").first().attr("abs:href");
    LinkedList<MatchEvent> matchEvents = returnMatchEvents(pbpURL);
    match = new Match(date, awayTeam, pointsAwayTeam, homeTeam, pointsHomeTeam, matchEvents);
    return match;
  }

  public LinkedList<Match> returnMatchesFromMonth(String url) {
    LinkedList<Match> matches = new LinkedList<Match>();
    Document doc = JsoupHelper.connectToLivePage(url);
    if (doc == null)
      return matches;

    Elements rows = doc.select("table#schedule tbody").first().select("tr");
    for (int i = 0; i < rows.size(); i++) {
      Element row = rows.get(i);
      Elements cols = row.select("td");
      if (cols.isEmpty())
        continue;
      Match match = returnMatch(row, cols);
      matches.add(match);
      System.out.println(match);
    }
    return matches;
  }

  public Season returnSeasonMatches(int year) {
    LinkedList<Match> seasonMatches = new LinkedList<Match>();
    LinkedList<Match> matches = null;
    Document doc = JsoupHelper.connectToLivePage(
        "https://www.basketball-reference.com/leagues/NBA_" + year + "_games.html");
    Season season = null;
    if (doc == null)
      return season;

    String months = doc.getElementsByClass("filter").first().text();
    String[] monthsArray = months.split(" ");

    for (int i = 0; i < monthsArray.length; i++) {
      String monthUrl = "https://www.basketball-reference.com/leagues/NBA_2019_games-"
          + monthsArray[i].toLowerCase() + ".html";
      matches = returnMatchesFromMonth(monthUrl);
      seasonMatches.addAll(matches);
    }
    System.out.println(seasonMatches);
    season = new Season(year, seasonMatches, null);

    return season;
  }

}
