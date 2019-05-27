package com.mozzartbet.gameservice.parser;

import java.util.LinkedList;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.mozzartbet.gameservice.domain.ActionType;
import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.MatchEvent;
import com.mozzartbet.gameservice.domain.Season;
import com.mozzartbet.gameservice.util.ConvertHelper;
import com.mozzartbet.gameservice.util.JsoupHelper;

public class JSoupMatchParser {
  String quarter = "";

  public MatchEvent returnMatchEvent(Element row) {
    MatchEvent matchEvent = null;
    Elements cols = row.select("td");
    String timestamp = cols.get(0).text();
    String awayTeamAction = cols.get(1).text();
    String scoreSummary;
    String homeTeamAction;
    String points;
    boolean pointsMade = false;
    Elements playersLink;
    ActionType[] actions;
    if (awayTeamAction.equals("\u00a0")) {
      points = cols.get(4).text();
      if (!points.isEmpty())
        pointsMade = true;
      homeTeamAction = cols.get(5).text();
      playersLink = cols.get(5).select("a");
      actions = ConvertHelper.returnActionType(homeTeamAction, pointsMade, playersLink);
      scoreSummary = cols.get(3).text();
      matchEvent =
          new MatchEvent(timestamp, scoreSummary, points, homeTeamAction, actions, quarter);
    } else {
      if (cols.size() == 2) {
        // neutralan dogadjaj
        matchEvent = new MatchEvent(awayTeamAction, timestamp, quarter);
        return matchEvent;
      }
      points = cols.get(2).text();
      if (!points.equals("\u00a0"))
        pointsMade = true;
      playersLink = cols.get(1).select("a");
      actions = ConvertHelper.returnActionType(awayTeamAction, pointsMade, playersLink);
      scoreSummary = cols.get(3).text();
      matchEvent =
          new MatchEvent(timestamp, awayTeamAction, actions, points, scoreSummary, quarter);
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
      System.out.println(matchEvent);
      matchEvents.add(matchEvent);

    }
    quarter = "1st";
    // setMatchEvents(matchEvents);
    return matchEvents;
  }

  public Match returnMatch(String id) {
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
    LinkedList<MatchEvent> matchEvents = returnMatchEvents(doc);
    match = new Match(date, awayTeam, pointsAwayTeam, homeTeam, pointsHomeTeam, matchEvents);
    // System.out.println(match);
    return match;
  }


  public Match returnOlderMatch(Element row, Elements cols) {
    Match match = null;
    String date = row.select("th").first().text();
    String awayTeam = cols.get(0).text();
    String pointsAwayTeam = cols.get(1).text();
    String homeTeam = cols.get(2).text();
    String pointsHomeTeam = cols.get(3).text();
    match = new Match(date, awayTeam, pointsAwayTeam, homeTeam, pointsHomeTeam, null);
    System.out.println(match);
    return match;
  }


  public LinkedList<Match> returnMatchesFromMonth(int year, String month) {
    LinkedList<Match> matches = new LinkedList<Match>();
    Match match = null;
    Document doc = JsoupHelper.connectToLivePage("https://www.basketball-reference.com/leagues/NBA_"
        + year + "_games-" + month.toLowerCase() + ".html");
    if (doc == null)
      return matches;

    Elements rows = doc.select("table#schedule tbody").first().select("tr");
    for (int i = 0; i < rows.size(); i++) {
      Element row = rows.get(i);
      Elements cols = row.select("td");
      if (cols.isEmpty() || cols.get(cols.size() - 4).text().isEmpty())
        continue;
      if (year < 2001)
        match = returnOlderMatch(row, cols);
      else {
        String matchId = cols.get(cols.size() - 4).select("a").attr("href");
        matchId = matchId.substring(11, matchId.length() - 5);
        // System.out.println(matchId);
        match = returnMatch(matchId);

      }
      matches.add(match);
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
      matches = returnMatchesFromMonth(year, monthsArray[i]);
      seasonMatches.addAll(matches);
    }
    season = new Season(year, seasonMatches, null);

    return season;
  }

}
