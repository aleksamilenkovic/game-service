package com.gameservice.repository;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gameservice.domain.MatchEvent;
import com.gameservice.mapper.MatchEventMapper;

@Repository
public class MatchEventRepository implements BaseRepository<MatchEvent> {
	@Autowired
	private MatchEventMapper matchEventMapper;

	@Override
	public long count() {
		return matchEventMapper.count();
	}

	@Override
	public MatchEvent getById(Long id) {
		return matchEventMapper.getById(id);
	}

	@Override
	public int insert(MatchEvent entity) throws SQLIntegrityConstraintViolationException {
		if (entity == null)
			return 0;
		return matchEventMapper.save(entity);
	}

	@Override
	public int update(MatchEvent entity) {
		return matchEventMapper.update(entity);
	}

	@Override
	public int deleteById(Long id) {
		return matchEventMapper.deleteById(id);
	}

	public List<MatchEvent> getEventsForQuarter(Long quarterId) {
		return matchEventMapper.getEventsFromQuarterId(quarterId);
	}

	public List<MatchEvent> findEvents(@Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate,
			@Param("sent") int sent) {
		return matchEventMapper.findEvents(fromDate, toDate, sent);
	}

	public List<MatchEvent> getEventsForMatchAndQuarter(String matchId, String quarterName) {
		return matchEventMapper.getEventsForMatchAndQuarter(matchId, quarterName);
	}

	public int updateSent(Long id) {
		return matchEventMapper.updateSent(id);
	}

	public long countSent(int sent) {
		return matchEventMapper.countSent(sent);
	}
}
