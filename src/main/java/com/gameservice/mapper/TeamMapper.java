package com.gameservice.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.mapping.FetchType;

import com.gameservice.domain.Player;
import com.gameservice.domain.Team;

@Mapper
public interface TeamMapper extends BaseMapper<Team> {

	@Select("select count(*) from basketball_teams")
	public long count();

	@Select("select id, created_on createdOn, modified_on modifiedOn, name,season_year seasonYear, team_id teamId from basketball_teams where id = #{id}")
	public Team getById(@Param("id") Long id);

	@Select("select id, created_on createdOn, modified_on modifiedOn, name,season_year seasonYear, team_id teamId from basketball_teams where team_id = #{teamId}")
	public Team getByTeamId(@Param("teamId") String teamId);

	@Insert("insert into basketball_teams(id, created_on, modified_on, name, season_year,team_id) values (#{id},current_timestamp, current_timestamp, #{name},#{seasonYear}, #{teamId})")
	@SelectKey(before = true, statement = "select sq_teams.nextval from dual", keyProperty = "id", resultType = Long.class)
	public int insert(Team entity);

	@Update("update basketball_teams set name=#{name},team_id=#{teamId}, modified_on=current_timestamp where team_id = #{teamId}")
	public int update(Team entity);

	@Delete("delete from basketball_teams where id = #{id}")
	public int deleteById(@Param("id") Long id);

	@Results(value = { @Result(id = true, property = "id", column = "id"),
			@Result(property = "createdOn", column = "created_on"),
			@Result(property = "modifiedOn", column = "modified_on"),
			@Result(property = "name", column = "player_name"), @Result(property = "number", column = "player_number"),
			@Result(property = "playerId", column = "player_id"), @Result(property = "team.id", column = "team_pk"),
			@Result(property = "team.teamId", column = "team_id") })
	@Select("select p.* from basketball_players p where p.team_id = #{teamId} order by p.id")
	public List<Player> getPlayersForTeam(@Param("teamId") String teamId);

	@Results(value = { @Result(id = true, property = "id", column = "id"),
			@Result(property = "createdOn", column = "created_on"),
			@Result(property = "modifiedOn", column = "modified_on"), @Result(property = "teamId", column = "team_id"),
			@Result(property = "name", column = "name"), @Result(property = "seasonYear", column = "season_year"),
			@Result(property = "players", javaType = List.class, column = "team_id", many = @Many(select = "getPlayersForTeam", fetchType = FetchType.LAZY)) })
	@Select("select t.* from basketball_teams t where t.team_id = #{id}")
	public Team getWithPlayersByTeamId(@Param("id") String id);

	@Results(value = { @Result(id = true, property = "id", column = "id"),
			@Result(property = "createdOn", column = "created_on"),
			@Result(property = "modifiedOn", column = "modified_on"), @Result(property = "teamId", column = "team_id"),
			@Result(property = "name", column = "name"), @Result(property = "seasonYear", column = "season_year"),
			@Result(property = "players", javaType = List.class, column = "team_id", many = @Many(select = "getPlayersForTeam", fetchType = FetchType.LAZY)) })
	@Select("select t.* from basketball_teams t where t.season_year = #{year}")
	public List<Team> getSeasonTeams(@Param("year") int year);

	/*
	 * @Results(value = {
	 * 
	 * @Result(id = true, property = "id", column = "id"),
	 * 
	 * @Result(property = "createdOn", column = "created_on"),
	 * 
	 * @Result(property = "modifiedOn", column = "modified_on"),
	 * 
	 * @Result(property = "name", column = "name"), @Result(property = "teamId",
	 * column = "team_id"),
	 * 
	 * @Result(property = "players", javaType = List.class, column = "id", many
	 * = @Many(select = "getPlayersForTeam", fetchType = FetchType.LAZY)) })
	 * 
	 * @Select("select t.* from teams t where id = #{id}") public Team
	 * getTeamWithPlayersById(@Param("teamId") String teamId);
	 * 
	 * @Results(value = {
	 * 
	 * @Result(id = true, property = "id", column = "id"),
	 * 
	 * @Result(property = "createdOn", column = "created_on"),
	 * 
	 * @Result(property = "modifiedOn", column = "modified_on"),
	 * 
	 * @Result(property = "playerId", column = "player_id"),
	 * 
	 * @Result(property = "teamId", column = "team_id"),
	 * 
	 * @Result(property = "number", column = "player_number"),
	 * 
	 * @Result(property = "name", column = "player_name"),
	 * 
	 * @Result(property = "position", column = "position"),
	 * 
	 * @Result(property = "height",column = "height"),
	 * 
	 * @Result(property = "weight", column = "weight"),
	 * 
	 * @Result(property = "birthDate", column = "birthDate"),
	 * 
	 * @Result(property = "nationality", column = "nationality"),
	 * 
	 * @Result(property = "experience", column = "experience"),
	 * 
	 * @Result(property = "college", column = "college") })
	 * 
	 * @Select("select p.* from players p where p.team_id = #{teamId}") public
	 * List<Player> getPlayersForTeam(@Param("teamId") String teamId);
	 */

}
