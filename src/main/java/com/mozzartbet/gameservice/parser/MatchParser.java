package com.mozzartbet.gameservice.parser;

import java.util.LinkedList;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.mozzartbet.gameservice.domain.ActionType;
import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.MatchEvent;
import com.mozzartbet.gameservice.domain.Season;
import com.mozzartbet.gameservice.exception.UrlException;
import com.mozzartbet.gameservice.util.ConvertHelper;
import com.mozzartbet.gameservice.util.JsoupHelper;

public class MatchParser {
  String quarter = "";

  public MatchEvent returnMatchEvent(Element row) {
    MatchEvent matchEvent = null;
    Elements cols = row.select("td");
    String timestamp = cols.get(0).text();
    String awayTeamAction = cols.get(1).text();
    String scoreSummary;
    String homeTeamAction;
    String points;
    int p = 0;
    boolean pointsMade = false;
    Elements playersLink;
    ActionType[] actions;
    if (awayTeamAction.equals("\u00a0")) {
      points = cols.get(4).text();
      if (!points.equals("\u00a0")) {
        points.substring(1);
        p = Integer.parseInt(points, 10);
        pointsMade = true;
      }
      homeTeamAction = cols.get(5).text();
      playersLink = cols.get(5).select("a");
      actions = ConvertHelper.returnActionType(homeTeamAction, pointsMade, playersLink);
      scoreSummary = cols.get(3).text();
      matchEvent = new MatchEvent(timestamp, scoreSummary, p, homeTeamAction, actions, quarter);
    } else {
      if (cols.size() == 2) {
        // neutralan dogadjaj
        matchEvent = new MatchEvent(awayTeamAction, timestamp, quarter);
        return matchEvent;
      }
      points = cols.get(2).text();
      if (!points.equals("\u00a0")) {
        points.substring(1);
        p = Integer.parseInt(points, 10);
        pointsMade = true;
      }
      playersLink = cols.get(1).select("a");
      actions = ConvertHelper.returnActionType(awayTeamAction, pointsMade, playersLink);
      scoreSummary = cols.get(3).text();
      matchEvent = new MatchEvent(timestamp, awayTeamAction, actions, p, scoreSummary, quarter);
    }

    return matchEvent;
  }

  public LinkedList<MatchEvent> returnMatchEvents(Document doc) {
    LinkedList<MatchEvent> matchEvents = new LinkedList<MatchEvent>();

    Elements rows = doc.select("table#pbp tr");
    for (int i = 0; i < rows.size(); i++) {
      Element row = rows.get(i);
      if (row.select("th").size() > 0) {
        if (row.childNodeSize() == 3)
          quarter = row.text();
        continue;
      }

      MatchEvent matchEvent = returnMatchEvent(row);
      // System.out.println(matchEvent);
      matchEvents.add(matchEvent);

    }
    quarter = "1st";
    // setMatchEvents(matchEvents);
    return matchEvents;
  }

  public Match returnMatch(String matchId, String fileName) {
    Match match = null;
    Document doc = null;
    String url = "https://www.basketball-reference.com/boxscores/pbp/" + matchId + ".html";
    int matchYear;
    if (!ConvertHelper.tryParseInt(matchId.substring(0, 4))) {
      return null;
    }
    matchYear = Integer.parseInt(matchId.substring(0, 4));
    if (matchYear < 2001) {
      return returnOlderMatch(matchId, fileName);

    }
    try {
      if (fileName == null) // AKO STAVIMO DA JE FILE NULL ONDA PARSIRAMO SA ONLINE STRANE
        doc = JsoupHelper.connectToLivePage(url);
      else // povezujemo se na lokalnu stranicu iz /src/test/resources/
        doc = JsoupHelper.connectToLocalPage(fileName);
    } catch (UrlException e) {
      System.out.println(e);
      return match;
    }

    Element scorebox = doc.getElementsByClass("scorebox").first();
    Elements teamNamesDiv = scorebox.select("strong");
    Elements teamPoints = scorebox.select("div.score");
    String awayTeam = teamNamesDiv.get(0).text();
    String homeTeam = teamNamesDiv.get(1).text();
    String[] dt = doc.select("div.scorebox_meta").select("div").first().text().split(" ");
    String date = dt[2] + dt[3] + dt[4];
    String pointsAwayTeam = teamPoints.select("div").get(0).text();
    String pointsHomeTeam = teamPoints.get(1).text();
    LinkedList<MatchEvent> matchEvents = returnMatchEvents(doc);
    match =
        new Match(date, awayTeam, pointsAwayTeam, homeTeam, pointsHomeTeam, matchEvents, matchId);
    // System.out.println(match);
    return match;
  }


  public Match returnOlderMatch(String matchId, String fileName) {
    /*
     * Match match = null; String date = row.select("th").first().text(); String awayTeam =
     * cols.get(0).text(); String pointsAwayTeam = cols.get(1).text(); String homeTeam =
     * cols.get(2).text(); String pointsHomeTeam = cols.get(3).text(); match = new Match(date,
     * awayTeam, pointsAwayTeam, homeTeam, pointsHomeTeam, null); // System.out.println(match);
     * return match;
     */
    Match match = null;
    Document doc = null;
    String url = "https://www.basketball-reference.com/boxscores/" + matchId + ".html";
    try {
      if (fileName == null) // AKO STAVIMO DA JE FILE NULL ONDA PARSIRAMO SA ONLINE STRANE
        doc = JsoupHelper.connectToLivePage(url);
      else // povezujemo se na lokalnu stranicu iz /src/test/resources/
        doc = JsoupHelper.connectToLocalPage(fileName);
    } catch (UrlException e) {
      System.out.println(e);
      return match;
    }
    Element scorebox = doc.getElementsByClass("scorebox").first();
    Elements teamNamesDiv = scorebox.select("strong");
    Elements teamPoints = scorebox.select("div.score");
    String awayTeam = teamNamesDiv.get(0).text();
    String homeTeam = teamNamesDiv.get(1).text();
    String[] dt = doc.select("div.scorebox_meta").select("div").first().text().split(" ");
    String date = dt[2] + dt[3] + dt[4];
    String pointsAwayTeam = teamPoints.select("div").get(0).text();
    String pointsHomeTeam = teamPoints.get(1).text();
    LinkedList<MatchEvent> matchEvents = returnMatchEvents(doc);
    match =
        new Match(date, awayTeam, pointsAwayTeam, homeTeam, pointsHomeTeam, matchEvents, matchId);
    // System.out.println(match);
    return match;
  }


  public LinkedList<Match> returnMatchesFromMonth(int year, String month) {
    LinkedList<Match> matches = new LinkedList<Match>();
    Match match = null;
    Document doc = null;
    try {
      doc = JsoupHelper.connectToLivePage("https://www.basketball-reference.com/leagues/NBA_" + year
          + "_games-" + month.toLowerCase() + ".html");
    } catch (UrlException e) {
      System.out.println(e);
      return matches;
    }
    Elements rows = doc.select("table#schedule tbody").first().select("tr");
    for (int i = 0; i < rows.size(); i++) {
      Element row = rows.get(i);
      Elements cols = row.select("td");
      if (cols.isEmpty() || cols.get(cols.size() - 4).text().isEmpty())
        continue;

      String matchId = cols.get(cols.size() - 4).select("a").attr("href");
      matchId = matchId.substring(11, matchId.length() - 5);
      // System.out.println(matchId);
      match = returnMatch(matchId, null);
      matches.add(match);
    }
    return matches;
  }

  public Season returnSeasonMatches(int year) {
    LinkedList<Match> seasonMatches = new LinkedList<Match>(), matches;
    Document doc;
    try {
      doc = JsoupHelper.connectToLivePage(
          "https://www.basketball-reference.com/leagues/NBA_" + year + "_games.html");
    } catch (UrlException e) {
      System.out.println(e);
      return null;
    }


    String months = doc.getElementsByClass("filter").first().text();
    String[] monthsArray = months.split(" ");

    for (int i = 0; i < monthsArray.length; i++) {
      matches = returnMatchesFromMonth(year, monthsArray[i]);
      seasonMatches.addAll(matches);
    }
    Season season = new Season(year, seasonMatches, null);

    return season;
  }

}
