package com.mozzartbet.gameservice.mapper;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import com.mozzartbet.gameservice.GameServiceApplicationTests;
import com.mozzartbet.gameservice.domain.Season;
import lombok.extern.slf4j.Slf4j;

@ActiveProfiles("test")
@Transactional
@Slf4j
public class SeasonMapperTest extends GameServiceApplicationTests {
  @Autowired
  private SeasonMapper seasonMapper;

  @Test
  @Commit
  public void testSeasons() {
    // saving all seasons first
    for (int i = 1960; i <= 2019; i++) {
      seasonMapper.insert(Season.builder().seasonYear(i).build());
    }
  }
}
