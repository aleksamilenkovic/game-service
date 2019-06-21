package com.mozzartbet.gameservice.parser;


import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.mozzartbet.gameservice.domain.Team;
import com.mozzartbet.gameservice.exception.UrlException;
import com.mozzartbet.gameservice.util.ConvertHelper;
import com.mozzartbet.gameservice.util.LoadPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
@RequiredArgsConstructor
public class TeamParserBasketballRef implements TeamParser {


  public Team returnTeamLive(String pageUrl) {
    return returnTeam(pageUrl, false);
  }

  public Team returnTeamLocal(String fileName) {
    return returnTeam(fileName, true);
  }

  public Team returnTeam(String pageUrl, boolean local) {
    Team team = null;
    // OVAKO SVUDA NAPRAVITI tj da li parsiramo za test iz lokala ili live(parseUrl)
    LoadPage lp = local ? LoadPage.parseFile(pageUrl) : LoadPage.parseUrl(pageUrl);
    try {
      Document doc = lp.parse();
      log.info(String.format("Parsing the team with url: %s", pageUrl));
      String teamId = ConvertHelper.returnTeamId(pageUrl, local),
          teamName = doc.select("[itemprop=name]").get(2).text();
      Elements rows = doc.select("table#roster tr");
      team = Team.builder().players((new PlayerParser()).returnPlayers(rows, teamId)).name(teamName)
          .teamId(teamId).build();
      log.info(String.format("Finished parsing the team: %s", teamName));
    } catch (UrlException e) {
      log.error(String.format("Error with returning the team with url:%s\n", pageUrl));
    }
    return team;
  }


  public List<Team> readTeamsFromSeason(int seasonYear) {
    String url =
        String.format("https://www.basketball-reference.com/leagues/NBA_%d.html", seasonYear);
    List<Team> teams = new ArrayList<Team>();
    try {
      LoadPage lp = LoadPage.parseUrl(url);
      Document doc = lp.parse();
      Elements rows = seasonYear > 1970 ? doc.select("table#divs_standings_E tr.full_table")
          : doc.select("table#divs_standings_ tr.full_table");

      for (int j = 0; j < 2; j++) {
        for (int i = 0; i < rows.size(); i++) {
          // dobijam sada sve redove jedne konferencije
          String link = rows.get(i).select("a").first().attr("abs:href");
          teams.add(returnTeam(link, false));
        }
        if (seasonYear <= 1970)
          break;
        rows = doc.select("table#divs_standings_W tr.full_table");
      }
    } catch (UrlException e) {
      e.printStackTrace();
    }
    return teams;
  }

  public Multimap<Integer, List<Team>> readTeamsFromSpecificSeasonTillNow(int year) {
    Multimap<Integer, List<Team>> allTeams = ArrayListMultimap.create();
    do {
      log.info(String.format(
          "**********************SEASON YEAR ----%s-----****************************** ", year));
      allTeams.put(year, readTeamsFromSeason(year));
    } while (year++ < 2019);
    return allTeams;
  }

}
