package com.mozzartbet.gameservice.service;


import org.springframework.beans.factory.annotation.Autowired;
import com.mozzartbet.gameservice.domain.Player;
import com.mozzartbet.gameservice.domain.Team;
import com.mozzartbet.gameservice.mapper.TeamMapper;
import com.mozzartbet.gameservice.parser.TeamParser;



// IDEJA JE DA SE U MEDJUVREMENU ODVOJI PARSER I SERVIS ZA SVAKU PARSER KLASU
// KAKO BI CEO SERVIS ZA CUVANJE TIMOVA I IGRACA BIO AGILAN UKOLIKO SE DODAJE PARSER ZA DRUGI SAJT
// TJ. DA SE U SERVISU PRAVE OBJEKTI IGRACA I TIMOVA(i odlucuje sta ce da se radi sa njima),
// DOK CE PARSER SAMO DA IH PROSLEDI (npr. u vidu
// stringa, niza stringova...)
// @Service
public class TeamService {
  @Autowired
  TeamParser teamParser;
  @Autowired
  TeamMapper teamMapper;

  public void saveTeam(String fileName) {
    Team team = teamParser.returnTeamLocal(fileName);
    teamMapper.insert(team);
  }

  public Team getTeam(String teamId) {
    return teamMapper.getByTeamId(teamId);
  }

  public void savePlayer(Player p) {}
}
