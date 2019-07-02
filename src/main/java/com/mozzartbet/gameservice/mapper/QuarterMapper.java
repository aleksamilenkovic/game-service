package com.mozzartbet.gameservice.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import com.mozzartbet.gameservice.domain.Quarter;

@Mapper
public interface QuarterMapper extends BaseMapper<Quarter> {
  @Select("select count(*) from basketball_quarters")
  public long count();

  @Select("select q.id id,q.created_on createdOn,q.modified_on modifiedOn,q.match_id matchId,q.quarter_name name,q.points_home_team pointsHomeTeam,q.points_away_team pointsAwayTeam from basketball_quarters q where q.id=#{id}")
  public Quarter getById(Long id);

  @Insert("insert into basketball_quarters(id,created_on,modified_on,match_id,quarter_name,points_home_team,points_away_team)"
      + "   values(#{id},current_timestamp,current_timestamp,#{match.matchId},#{name},#{pointsHomeTeam},#{pointsAwayTeam})")
  @SelectKey(before = true, statement = "select sq_quarters.nextval from dual", keyProperty = "id",
      resultType = Long.class)
  public int insert(Quarter entity);

  @Update("update basketball_quarters set name=#{name}, modified_on=current_timestamp,points_home_team=#{pointsHomeTeam},points_away_team=#{pointsAwayTeam} where id=#{id}")
  public int update(Quarter entity);

  @Delete("delete from basketball_quarters where id = #{id}")
  public int deleteById(Long id);


}
