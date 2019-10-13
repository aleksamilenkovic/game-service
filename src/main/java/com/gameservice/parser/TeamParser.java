package com.gameservice.parser;

import java.util.List;

import com.gameservice.domain.Team;
import com.google.common.collect.Multimap;

public interface TeamParser {
	public Team returnTeamLive(String pageUrl);

	public Team returnTeamLocal(String fileName);

	public List<Team> readTeamsFromSeason(int seasonYear);

	public Multimap<Integer, List<Team>> readTeamsFromSpecificSeasonTillNow(int year);
}
