package com.mozzartbet.gameservice.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SeasonServiceTest extends BaseServiceTest {
  @Autowired
  private SeasonService seasonService;

  @Commit
  @Test
  public void testSavingSeason() {
    seasonService.parseAndSaveAll(1972);
  }

  @Commit
  @Test
  public void testSavingAllSeasons() {
    for (int i = 2005; i <= 2018; i++)
      seasonService.parseAndSaveAll(i);
  }
}
