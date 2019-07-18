package com.mozzartbet.gameservice.parser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.MatchEvent;
import com.mozzartbet.gameservice.domain.MatchEventType;
import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.domain.Quarter;
import com.mozzartbet.gameservice.domain.Season;
import com.mozzartbet.gameservice.domain.Team;
import com.mozzartbet.gameservice.exception.UrlException;
import com.mozzartbet.gameservice.parser.threads.MonthMatchParserThreads;
import com.mozzartbet.gameservice.parser.threads.SeasonMatchParserThreads;
import com.mozzartbet.gameservice.util.ConvertHelper;
import com.mozzartbet.gameservice.util.JsoupHelper;
import com.mozzartbet.gameservice.util.LoadPage;
import com.mozzartbet.gameservice.util.MatchEventHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
public class MatchParserBasketballRef {
  String quarter = "1st Q";

  public MatchEvent returnMatchEvent(Element row, Quarter quarter) {
    MatchEvent matchEvent = null;
    Elements cols = row.select("td"), playersLink;
    String time = cols.get(0).text(), awayTeamAction = cols.get(1).text(), scoreSummary,
        homeTeamAction, p;
    Player players[] = new Player[2];
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
      players = MatchEventHelper.returnPlayers(playersLink, quarter.getMatch());
      if (players != null)
        actionType = MatchEventHelper.returnActionType(homeTeamAction, pointsMade, timestamp);
      scoreSummary = cols.get(3).text();
      matchEvent = MatchEvent.createForHomeTeam(scoreSummary, timestamp, points, homeTeamAction,
          actionType, players, quarter);
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

      players = MatchEventHelper.returnPlayers(playersLink, quarter.getMatch());
      if (players != null)
        actionType = MatchEventHelper.returnActionType(awayTeamAction, pointsMade, timestamp);
      scoreSummary = cols.get(3).text();
      matchEvent = MatchEvent.createForAwayTeam(scoreSummary, timestamp, points, awayTeamAction,
          actionType, players, quarter);
    }

    return matchEvent;
  }

  public List<Quarter> returnMatchEvents(Document doc, Match match) {
    List<Quarter> quarters = new ArrayList<Quarter>();
    List<MatchEvent> quarterEvents = new ArrayList<MatchEvent>();
    // int i = -1;
    Elements rows = doc.select("table#pbp tr");
    // rows.remove(0);
    Quarter q = new Quarter(quarter, quarterEvents, match);
    // new Quarter(quarter, quarterEvents, match);
    for (Element row : rows) {
      log.info(row.text());
      if (row.childNodeSize() <= 3 && row.select("td").size() == 1)
        continue;
      if (row.select("th").size() > 0) {
        if (row.childNodeSize() == 3) {
          q.calculateResult();
          // if (!quarter.equals("1st Q")) {
          quarter = row.text();
          quarterEvents = new ArrayList<MatchEvent>();
          q = new Quarter(quarter, quarterEvents, match);
          // }
          // if (quarterEvents.size() > 1)
          boolean alreadyExist = false;
          // OVA PETLJA JE NAPRAVLJENA ZATO STO POSTOJI BAG NA basketball-ref
          // desava se da ide npr. 3.cetvrtina pa 4., pa opet 3. pa 4. (ova petlja ce imati ne vise
          // od 5,6 iteracija tako da ne smanjuje brzinu programa gotovo uopste)
          for (Quarter quart : quarters)
            if (quart.getName().equals(q.getName())) {
              q = quart;
              quarterEvents = quart.getMatchEvents();
              alreadyExist = true;
            }
          if (!alreadyExist)
            quarters.add(q);
        }
        continue;
      }
      // System.out.println(returnMatchEvent(row));
      quarterEvents.add(returnMatchEvent(row, q));
    }
    // Quarter lastQuarter = new Quarter(quarter, quarterEvents, match);
    // lastQuarter.calculateResult();
    q.calculateResult();
    // quarters.add(lastQuarter);
    quarter = "1st Q";
    // setMatchEvents(matchEvents);

    return quarters;
  }



  public void returnPlayerIds(String matchId, List<String> homePlayers, List<String> awayPlayers,
      int year) {
    if (year < 2001)
      return;
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
    LocalDateTime datetime = ConvertHelper.convertStringToLocalDate(matchId.substring(0, 8));
    int year = datetime.getMonthValue() > 7 ? datetime.getYear() + 1 : datetime.getYear();
    String url = String.format("https://www.basketball-reference.com/boxscores/%s.html",
        year >= 2001 ? "pbp/" + matchId : matchId);
    try {
      doc = fileName == null ? JsoupHelper.connectToLivePage(url)
          : JsoupHelper.connectToLocalPage(fileName);
    } catch (UrlException e) {
      System.out.println(e);
      return match;
    }

    Element scorebox = doc.getElementsByClass("scorebox").first();
    Elements teamNamesDiv = scorebox.select("strong"), teamPoints = scorebox.select("div.score");
    String pointsAwayTeam = teamPoints.select("div").get(0).text(),
        pointsHomeTeam = teamPoints.get(1).text();
    // String[] dt = doc.select("div.scorebox_meta").select("div").first().text().split(" ");
    // LocalDateTime datetime = ConvertHelper.convertStringWithFullMonthToLocalDate(dt, year <
    // 2001);
    Team awayTeam =
        Team.builder().name(teamNamesDiv.get(0).text())
            .teamId(ConvertHelper.returnTeamAttrRefId(teamNamesDiv.get(0))).build(),
        homeTeam = Team.builder().name(teamNamesDiv.get(1).text())
            .teamId(ConvertHelper.returnTeamAttrRefId(teamNamesDiv.get(1))).build();
    match = new Match(datetime, awayTeam, pointsAwayTeam, homeTeam, pointsHomeTeam, null, matchId,
        year);
    returnPlayerIds(matchId, match.getHomePlayersID(), match.getAwayPlayersID(), year);
    List<Quarter> quarters = year >= 2001 ? returnMatchEvents(doc, match) : null;
    match.setQuarters(quarters);



    if (year >= 2001)
      match.calculateStats();

    return match;
  }

  public List<Match> returnMatchesFromMonth(int year, String month) {
    List<Match> matches = new ArrayList<Match>();
    Document doc = null;
    try {
      doc = JsoupHelper.connectToLivePage(
          String.format("https://www.basketball-reference.com/leagues/NBA_%d_games-%s.html", year,
              month.toLowerCase()));
    } catch (UrlException e) {
      System.out.println(e);
      return matches;
    }

    /*
     * Elements rows = doc.select("table#schedule tbody").first().select("tr"); for (int i = 0; i <
     * rows.size(); i++) { Element row = rows.get(i); Elements cols = row.select("td"); if
     * (cols.isEmpty() || cols.get(cols.size() - 4).text().isEmpty()) continue;
     * 
     * String matchId = cols.get(cols.size() - 4).select("a").attr("href"); matchId =
     * matchId.substring(11, matchId.length() - 5); // System.out.println(matchId); Match match =
     * returnMatch(matchId, null, year); matches.add(match); }
     */
    MonthMatchParserThreads monthThreads = new MonthMatchParserThreads();

    // return matches;
    return monthThreads.returnAllMonthMatches(doc);
  }



  public Season returnSeasonWithMatches(int year) throws UrlException {
    List<Match> seasonMatches = new ArrayList<Match>(), matches;
    String url =
        String.format("https://www.basketball-reference.com/leagues/NBA_%d_games.html", year);
    LoadPage lp = LoadPage.parseUrl(url);
    Document doc = lp.parse();

    String months = doc.getElementsByClass("filter").first().text();
    SeasonMatchParserThreads threadService = new SeasonMatchParserThreads();
    seasonMatches = threadService.returnAllMatchesFromMonth(year, months.split(" "));
    // String[] monthsArray = months.split(" ");

    // for (int i = 0; i < monthsArray.length; i++) {

    /*
     * matches = returnMatchesFromMonth(year, monthsArray[i]); seasonMatches.addAll(matches);
     */
    // }
    Season season =
        Season.builder().seasonYear(year).seasonMatches(seasonMatches).teams(null).build();

    return season;
  }

  public List<Match> returnSeasonMatches(int year) throws UrlException {
    List<Match> seasonMatches = new ArrayList<Match>(), matches;
    String url =
        String.format("https://www.basketball-reference.com/leagues/NBA_%d_games.html", year);
    LoadPage lp = LoadPage.parseUrl(url);
    Document doc = lp.parse();

    String months = doc.getElementsByClass("filter").first().text();
    SeasonMatchParserThreads threadService = new SeasonMatchParserThreads();
    seasonMatches = threadService.returnAllMatchesFromMonth(year, months.split(" "));
    return seasonMatches;
    // String[] monthsArray = months.split(" ");

    // for (int i = 0; i < monthsArray.length; i++) {

    /*
     * matches = returnMatchesFromMonth(year, monthsArray[i]); seasonMatches.addAll(matches);
     */
    // }
  }

}
