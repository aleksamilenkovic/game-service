package com.gameservice.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gameservice.domain.Season;
import com.gameservice.mapper.SeasonMapper;

@Repository
public class SeasonRepository implements BaseRepository<Season> {
	@Autowired
	private SeasonMapper seasonMapper;

	@Override
	public long count() {
		return seasonMapper.count();
	}

	@Override
	public Season getById(Long id) {
		return seasonMapper.getById(id);
	}

	@Override
	public int insert(Season entity) {
		if (entity == null)
			return 0;
		Season season = getByYear(entity.getSeasonYear());
		return season != null ? 0 : seasonMapper.save(entity);
	}

	@Override
	public int update(Season entity) {
		return seasonMapper.update(entity);
	}

	@Override
	public int deleteById(Long id) {
		return seasonMapper.deleteById(id);
	}

	public Season getByYear(int year) {
		return seasonMapper.getByYear(year);
	}

}
