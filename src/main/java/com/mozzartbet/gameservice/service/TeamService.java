package com.mozzartbet.gameservice.service;

import java.util.List;
import com.mozzartbet.gameservice.domain.Team;

// IDEJA JE DA SE U MEDJUVREMENU ODVOJI PARSER I SERVIS ZA SVAKU PARSER KLASU
// KAKO BI CEO SERVIS ZA CUVANJE TIMOVA I IGRACA BIO AGILAN UKOLIKO SE DODAJE PARSER ZA DRUGI SAJT
// TJ. DA SE U SERVISU PRAVE OBJEKTI IGRACA I TIMOVA(i odlucuje sta ce da se radi sa njima),
// DOK CE PARSER SAMO DA IH PROSLEDI (npr. u vidu
// stringa, niza stringova...)
// @Service
public interface TeamService {

  public int save(Team team);

  public Team getTeam(Long id);

  public Team parseTeam(String teamId);

  public List<Team> parseSeasonTeams(int year);
}
