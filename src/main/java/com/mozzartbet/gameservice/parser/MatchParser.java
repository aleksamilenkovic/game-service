package com.mozzartbet.gameservice.parser;

import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.MatchEvent;
import com.mozzartbet.gameservice.domain.MatchEventType;
import com.mozzartbet.gameservice.domain.Quarter;
import com.mozzartbet.gameservice.domain.Season;
import com.mozzartbet.gameservice.exception.UrlException;
import com.mozzartbet.gameservice.util.MatchEventHelper;
import lombok.extern.slf4j.Slf4j;
import com.mozzartbet.gameservice.util.ConvertHelper;
import com.mozzartbet.gameservice.util.JsoupHelper;
import com.mozzartbet.gameservice.util.LoadPage;

@Slf4j
public class MatchParser {
  String quarter = "1st Q";

  public MatchEvent returnMatchEvent(Element row) {
    MatchEvent matchEvent = null;
    Elements cols = row.select("td"), playersLink;
    String time = cols.get(0).text(), awayTeamAction = cols.get(1).text(), scoreSummary,
        homeTeamAction, p;
    String players[] = new String[2];
    float timestamp = Float.parseFloat(time.substring(0, time.length() - 2).replace(':', '.'));
    int points = 0;
    boolean pointsMade = false;
    MatchEventType actionType = null;

    if (awayTeamAction.isEmpty() || awayTeamAction.equals("\u00a0")) {
      p = cols.get(4).text();
      if (!p.isEmpty() && !p.equals("\u00a0")) {
        p.substring(1);
        points = Integer.parseInt(p, 10);
        pointsMade = true;
      }
      homeTeamAction = cols.get(5).text();
      playersLink = cols.get(5).select("a");
      players = MatchEventHelper.returnPlayersIds(playersLink);
      if (players != null)
        actionType = MatchEventHelper.returnActionType(homeTeamAction, pointsMade, timestamp);
      scoreSummary = cols.get(3).text();
      matchEvent = MatchEvent.createForHomeTeam(scoreSummary, timestamp, points, homeTeamAction,
          actionType, quarter, players);
    } else {
      if (cols.size() == 2) {
        // neutralan dogadjaj
        matchEvent = new MatchEvent(awayTeamAction, timestamp, quarter);
        return matchEvent;
      }
      p = cols.get(2).text();
      if (!p.isEmpty() && !p.equals("\u00a0")) {
        p.substring(1);
        points = Integer.parseInt(p, 10);
        pointsMade = true;
      }
      playersLink = cols.get(1).select("a");

      players = MatchEventHelper.returnPlayersIds(playersLink);
      if (players != null)
        actionType = MatchEventHelper.returnActionType(awayTeamAction, pointsMade, timestamp);
      scoreSummary = cols.get(3).text();
      matchEvent = MatchEvent.createForAwayTeam(scoreSummary, timestamp, points, awayTeamAction,
          actionType, quarter, players);
    }

    return matchEvent;
  }

  public List<Quarter> returnMatchEvents(Document doc) {
    List<Quarter> quarters = new ArrayList<Quarter>();
    List<MatchEvent> quarterEvents = new ArrayList<MatchEvent>();;
    // int i = -1;
    Elements rows = doc.select("table#pbp tr");
    rows.remove(0);
    for (Element row : rows) {
      log.info(row.text());
      if (row.select("th").size() > 0) {
        if (row.childNodeSize() == 3) {
          quarters.add(new Quarter(quarter, quarterEvents));
          quarterEvents = new ArrayList<MatchEvent>();
          quarter = row.text();
        }
        continue;
      }
      // System.out.println(returnMatchEvent(row));
      quarterEvents.add(returnMatchEvent(row));
    }
    quarters.add(new Quarter(quarter, quarterEvents));
    quarter = "1st Q";
    // setMatchEvents(matchEvents);
    return quarters;
  }

  public void returnPlayerIds(String matchId, List<String> homePlayers, List<String> awayPlayers) {
    Document doc = null;
    String url = "https://www.basketball-reference.com/boxscores/" + matchId + ".html";
    try {
      doc = JsoupHelper.connectToLivePage(url);
    } catch (UrlException e) {
      System.out.println(e);
      return;
    }
    Elements tables = doc.getElementsByClass("sortable");
    for (int i = 0; i < 3; i += 2) {
      Elements rows = tables.get(i).select("tbody tr:not(.thead) th");
      for (Element row : rows) {
        String playerID = ConvertHelper.returnPlayerId(row.select("a").first().attr("abs:href"));
        if (i == 0)
          awayPlayers.add(playerID);
        else
          homePlayers.add(playerID);
      }
    }

  }

  public Match returnMatch(String matchId, String fileName) {
    Match match = null;
    Document doc = null;
    String url = "https://www.basketball-reference.com/boxscores/pbp/" + matchId + ".html";
    try {
      int matchYear = ConvertHelper.tryParseInt(matchId.substring(0, 4));
      if (matchYear < 2001)
        return returnOlderMatch(matchId, fileName);
      doc = fileName == null ? JsoupHelper.connectToLivePage(url)
          : JsoupHelper.connectToLocalPage(fileName);
    } catch (UrlException e) {
      System.out.println(e);
      return match;
    }

    Element scorebox = doc.getElementsByClass("scorebox").first();
    Elements teamNamesDiv = scorebox.select("strong"), teamPoints = scorebox.select("div.score");
    String awayTeam = teamNamesDiv.get(0).text(), homeTeam = teamNamesDiv.get(1).text(),
        pointsAwayTeam = teamPoints.select("div").get(0).text(),
        pointsHomeTeam = teamPoints.get(1).text();
    String[] dt = doc.select("div.scorebox_meta").select("div").first().text().split(" ");
    String date = dt[2] + dt[3] + dt[4];
    List<Quarter> quarters = returnMatchEvents(doc);
    match = new Match(date, awayTeam, pointsAwayTeam, homeTeam, pointsHomeTeam, quarters, matchId);
    returnPlayerIds(matchId, match.getHomePlayersID(), match.getAwayPlayersID());
    return match;
  }


  public Match returnOlderMatch(String matchId, String fileName) {
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
    Elements teamNamesDiv = scorebox.select("strong"), teamPoints = scorebox.select("div.score");
    String awayTeam = teamNamesDiv.get(0).text(), homeTeam = teamNamesDiv.get(1).text(),
        pointsAwayTeam = teamPoints.select("div").get(0).text(),
        pointsHomeTeam = teamPoints.get(1).text();
    String[] dt = doc.select("div.scorebox_meta").select("div").first().text().split(" ");
    String date = dt[2] + dt[3] + dt[4];

    match = new Match(date, awayTeam, pointsAwayTeam, homeTeam, pointsHomeTeam, null, matchId);
    // System.out.println(match);
    return match;
  }


  public List<Match> returnMatchesFromMonth(int year, String month) {
    List<Match> matches = new ArrayList<Match>();
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

  public Season returnSeasonMatches(int year) throws UrlException {
    List<Match> seasonMatches = new ArrayList<Match>(), matches;
    String url =
        String.format("https://www.basketball-reference.com/leagues/NBA_%d_games.html", year);
    LoadPage lp = LoadPage.parseUrl(url);
    Document doc = lp.parse();

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
