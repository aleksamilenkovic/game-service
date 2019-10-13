package com.gameservice.service.impl;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.gameservice.domain.Match;
import com.gameservice.domain.MatchEvent;
import com.gameservice.domain.Player;
import com.gameservice.domain.Quarter;
import com.gameservice.domain.Team;
import com.gameservice.exception.UrlException;
import com.gameservice.parser.MatchParserBasketballRef;
import com.gameservice.repository.MatchEventRepository;
import com.gameservice.repository.MatchRepository;
import com.gameservice.repository.PlayerRepository;
import com.gameservice.repository.PlayerStatsRepository;
import com.gameservice.repository.QuarterRepository;
import com.gameservice.repository.TeamRepository;
import com.gameservice.service.MatchService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchServiceImpl implements MatchService {
	final MatchRepository matchRepo;
	final QuarterRepository quarterRepo;
	final MatchEventRepository eventRepo;
	final PlayerRepository playerRepo;
	final PlayerStatsRepository playerStatsRepo;
	final MatchParserBasketballRef matchParser = new MatchParserBasketballRef();
	final TeamRepository teamRepo;

	@Override
	public int save(Match match) {
		return match.getQuarters() != null ? saveMatchWithEvents(match) : matchRepo.insert(match);
	}

	@Override
	public Match getMatch(String matchId) {
		return matchRepo.getByMatchId(matchId);
	}

	@Override
	public Match parseMatch(String matchId) {
		return (matchId == null || matchId.length() < 10) ? null : matchParser.returnMatch(matchId, null);
	}

	@Override
	public List<Match> parseSeasonMatches(int year) {
		try {
			return matchParser.returnSeasonMatches(year);
		} catch (UrlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private int saveMatchWithEvents(Match match) {
		if (match == null)
			return 0;
		if (matchRepo.insert(match) == 0)
			return 0;
		for (Quarter q : match.getQuarters()) {
			quarterRepo.insert(q);
			for (MatchEvent event : q.getMatchEvents()) {
				try {
					eventRepo.insert(event);
				} catch (Exception sqlException) {
					insertNewPlayer(event);
				}
			}
		}
		if (match.getMatchStats() != null) {
			playerStatsRepo.insertMatchStats(match.getMatchStats().getHomeTeamPlayerStats(),
					match.getMatchStats().getAwayTeamPlayerStats());
		}
		return 1;
	}

	private void insertNewPlayer(MatchEvent event) {
		// AKO SE DESI DA JE IGRAC PRESAO U NEKI TIM U TOKU SEZONE, PA GA NEMA U TOM
		// TIMU, ALI GA IMA
		// U ROSTERU
		// try {
		if (event.getFirstPlayer() != null & playerRepo.getByPlayerIdAndTeamId(event.getFirstPlayer().getPlayerId(),
				event.getFirstPlayer().getTeam().getTeamId()) == null) {
			Team team = event.getFirstPlayer().getTeam();
			Player newPlayer = playerRepo.getPlayersInfo(event.getFirstPlayer().getPlayerId());
			// log.info(newPlayer.toString());
			if (newPlayer != null) {
				newPlayer.setTeam(team);
				playerRepo.insert(newPlayer);
			} else
				event.setFirstPlayer(null);
		} else if (event.getSecondPlayer() != null
				&& playerRepo.getByPlayerIdAndTeamId(event.getSecondPlayer().getPlayerId(),
						event.getSecondPlayer().getTeam().getTeamId()) == null) {
			Team team = event.getSecondPlayer().getTeam();
			Player newPlayer = playerRepo.getPlayersInfo(event.getSecondPlayer().getPlayerId());
			if (newPlayer != null) {
				newPlayer.setTeam(team);
				playerRepo.insert(newPlayer);
			} else
				event.setSecondPlayer(null);
		}
		try {
			eventRepo.insert(event);
		} catch (SQLIntegrityConstraintViolationException sqlException) {
			log.info("New player in event [can't parse him and insert]");
		}
		// } catch (Exception e) {
		// log.info("Coach is getting tech.");
		// }
	}

	@Override
	public List<Match> getSeasonMatches(int year) {
		return matchRepo.getSeasonMatches(year);
	}
}
