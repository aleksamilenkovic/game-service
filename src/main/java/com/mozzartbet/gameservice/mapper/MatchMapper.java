package com.mozzartbet.gameservice.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.FetchType;
import com.mozzartbet.gameservice.domain.Match;
import com.mozzartbet.gameservice.domain.Quarter;

@Mapper
public interface MatchMapper extends BaseMapper<Match> {

  public long count();

  public Match getById(Long id);

  public Match getByMatchId(String matchId);

  public int insert(Match entity);

  public int update(Match entity);

  public int deleteById(Long id);


  @Results(value = {
      @Result(id = true, property = "id", column = "id"),
      @Result(property = "createdOn", column = "created_on"),
      @Result(property = "modifiedOn", column = "modified_on"),
      @Result(property = "name", column = "quarter_name"),
      @Result(property = "match.matchId", column = "match_id"),
      @Result(property = "pointsHomeTeam", column = "points_home_team"),
      @Result(property = "pointsAwayTeam", column = "points_away_team")
  })
  @Select("select q.* from basketball_quarters q where q.match_id = #{matchId} order by id")
  public List<Quarter> getQuartersByMatchId(@Param("matchId") String matchId);

  @Results(value = {
      @Result(id = true, property = "id", column = "id"),
      @Result(property = "createdOn", column = "created_on"),
      @Result(property = "modifiedOn", column = "modified_on"),
      @Result(property = "matchId", column = "match_id"),
      @Result(property = "dateTime", column = "date_time"),
      @Result(property = "seasonYear", column = "season_year"),
      @Result(property = "awayTeam.id", column = "away_id"),
      @Result(property = "homeTeam.id", column = "home_id"),
      @Result(property = "awayTeam.teamId", column = "away_team_id"),
      @Result(property = "homeTeam.teamId", column = "home_team_id"),
      @Result(property = "finalScore", column = "final_score"),
      @Result(property = "homeTeamPoints", column = "home_team_points"),
      @Result(property = "awayTeamPoints", column = "away_team_points"),
      @Result(property = "quarters", javaType = List.class, column = "match_id",
          many = @Many(select = "getQuartersByMatchId", fetchType = FetchType.LAZY))
  })
  @Select("select m.* from basketball_matches m where m.match_id = #{matchId}")
  public Match getWithQuartersByMatchId(@Param("matchId") String matchId);

}
