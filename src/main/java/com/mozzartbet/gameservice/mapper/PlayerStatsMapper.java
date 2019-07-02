package com.mozzartbet.gameservice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.mozzartbet.gameservice.domain.boxscore.PlayerStats;

@Mapper
public interface PlayerStatsMapper extends BaseMapper<PlayerStats> {

  public long count();

  public PlayerStats getById(@Param("id") Long id);

  public int insert(@Param("playerStats") PlayerStats playerStats);

  public int update(@Param("playerStats") PlayerStats playerStats);

  public int deleteById(@Param("id") Long id);

  public PlayerStats getByMatchIdAndPlayerId(@Param("playerId") String playerId,
      @Param("matchId") String matchId);

}
