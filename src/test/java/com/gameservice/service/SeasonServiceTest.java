package com.gameservice.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;

import com.gameservice.domain.Season;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SeasonServiceTest extends BaseServiceTest {
	@Autowired
	private SeasonService seasonService;

	@Commit
	@Test
	public void testSavingSeason() {
		seasonService.parseAndSaveAll(2012);
	}

	@Commit
	@Test
	public void testSavingAllSeasons() {
		for (int i = 2019; i >= 1990; i--)
			seasonService.parseAndSaveAll(i);
	}

	@Test
	public void testParsingSeason() {

	}

	@Test
	public void testFetchingSeason() {
		Season season = seasonService.getWithAllTeamsAndMatches(2019);

	}
}
