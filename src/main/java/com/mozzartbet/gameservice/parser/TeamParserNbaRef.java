package com.mozzartbet.gameservice.parser;


import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.mozzartbet.gameservice.domain.Team;
import com.mozzartbet.gameservice.exception.UrlException;
import com.mozzartbet.gameservice.util.ConvertHelper;
import com.mozzartbet.gameservice.util.LoadPage;
import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class TeamParserNbaRef implements TeamParser{
	
  
  public Team returnTeamLive(String pageUrl, String teamName) {
   return returnTeam(pageUrl,teamName, false);
  }
  public Team returnTeamLocal(String fileName,String teamName) {
    return returnTeam(fileName,teamName, true);
  }
  public Team returnTeam(String pageUrl, String teamName, boolean local){
    Team t = new Team();
    t.setTeamName(teamName);
    // OVAKO SVUDA NAPRAVITI tj da li parsiramo za test iz lokala ili live(parseUrl)
	LoadPage lp = local?LoadPage.parseFile(pageUrl):LoadPage.parseUrl(pageUrl);
    Document doc;
	try {
		doc = lp.parse();
	log.info(String.format("Parsing the team: %s", teamName));
    String teamId = ConvertHelper.returnTeamId(pageUrl, local);
    Elements rows = doc.select("table#roster tr");
    t.setPlayers((new PlayerParser()).returnPlayers(rows, teamId));
	} catch (UrlException e) {
		log.error(String.format("Error with returning the team with url:%s\n team name:%s",pageUrl,teamName));
	}
    return t;
  }


  public List<Team> readTeamsFromSeason(int seasonYear) {
    String url = "https://www.basketball-reference.com/leagues/NBA_" + seasonYear + ".html";
    List<Team> teams = new ArrayList<Team>();
    LoadPage lp = LoadPage.parseUrl(url);
    Document doc;
	try {
		doc = lp.parse();
	
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

        Team t = returnTeam(link, firstColumn.text(),false);
        // t.showTeam();
        teams.add(t);
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
      log.info(String.format("**********************SEASON YEAR ----%s-----****************************** ",year));
      allTeams.put(year, readTeamsFromSeason(year));
    } while (year++ < 2019);
    return allTeams;
  }

}
