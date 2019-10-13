package com.gameservice.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.gameservice.domain.MatchEvent;

@Mapper
public interface MatchEventMapper extends BaseMapper<MatchEvent> {

	public long count();

	public MatchEvent getById(Long id);

	public int insert(MatchEvent entity);

	public int update(MatchEvent entity);

	public int deleteById(Long id);

	public List<MatchEvent> findEvents(@Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate,
			@Param("sent") int sent);

	public List<MatchEvent> getEventsFromQuarterId(Long id);

	public List<MatchEvent> getEventsForMatchAndQuarter(String matchId, String quarterName);

	public int updateSent(Long id);

	public long countSent(int sent);
}
