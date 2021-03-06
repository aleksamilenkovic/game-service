package com.gameservice.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gameservice.domain.Match;
import com.gameservice.domain.Quarter;
import com.gameservice.mapper.MatchEventMapper;
import com.gameservice.mapper.MatchMapper;
import com.gameservice.mapper.QuarterMapper;

@Repository
public class MatchRepository implements BaseRepository<Match> {
	@Autowired
	private MatchMapper matchMapper;
	@Autowired
	private QuarterMapper quarterMapper;
	@Autowired
	private MatchEventMapper eventMapper;

	@Override
	public long count() {
		return matchMapper.count();
	}

	@Override
	public Match getById(Long id) {
		return matchMapper.getById(id);
	}

	public Match getByMatchId(String matchId) {
		return matchMapper.getByMatchId(matchId);
	}

	@Override
	public int insert(Match entity) {
		if (entity == null)
			return 0;
		Match player = getByMatchId(entity.getMatchId());
		return (player != null && entity.getId() != null) ? 0 : matchMapper.save(entity);
	}

	@Override
	public int update(Match entity) {
		return matchMapper.update(entity);
	}

	@Override
	public int deleteById(Long id) {
		return matchMapper.deleteById(id);
	}

	public List<Quarter> getQuartersByMatchId(String matchId) {
		return matchMapper.getQuartersByMatchId(matchId);
	}

	public List<Match> getSeasonMatches(int year) {
		return matchMapper.getSeasonMatchesWithEvents(year);
	}

}
