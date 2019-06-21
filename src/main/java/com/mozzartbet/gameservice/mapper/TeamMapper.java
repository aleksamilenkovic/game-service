package com.mozzartbet.gameservice.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import com.mozzartbet.gameservice.domain.Team;

@Mapper
public interface TeamMapper extends BaseMapper<Team> {

  @Select("select count(*) from teams")
  public long count();

  @Select("select id, created_on createdOn, modified_on modifiedOn, name, team_id teamId from teams where team_id = #{teamId}")
  public Team getById(@Param("teamId") String teamId);

  @Insert("insert into teams(id,team_id, created_on, modified_on, name) values (#{id}, #{teamId},current_timestamp, current_timestamp, #{name})")
  @SelectKey(before = true, statement = "select sq_teams.nextval from dual", keyProperty = "id",
      resultType = Long.class)
  public int save(Team entity);

  @Update("update teams set name=#{name}, modified_on=current_timestamp where team_id = #{teamId}")
  public int update(Team entity);

  @Delete("delete from teams where id = #{id}")
  public int deleteById(@Param("id") Long id);


}
