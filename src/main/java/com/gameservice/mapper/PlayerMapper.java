package com.gameservice.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.gameservice.domain.Player;
import com.gameservice.domain.Team;
import com.gameservice.service.dto.PlayerSearchRequest;

@Mapper
public interface PlayerMapper extends BaseMapper<Player> {
	// @Select("select count(*) from players")
	public long count();

	// @Select("select id, playerId, teamId, created_on createdOn, modified_on
	// modifiedOn,
	// number,name,position,height,weight"
	// + ",birthDate,nationality,expirience,college from players where playerId =
	// #{playerId}")
	public Player getById(@Param("id") Long id);

	public Player getByPlayerIdAndTeamId(@Param("playerId") String playerId, @Param("teamId") String teamId);

	// @Insert("insert into players(id, playerId, teamId, created_on , modified_on ,
	// number,name,position,height,weight,birthDate,nationality,expirience,college)
	// "
	// + "values (#{id}, #{playerId},#{teamId},current_timestamp,
	// current_timestamp,#{number},
	// #{name}, #{position},#{height}, #{weight}, #{birthDate},#{nationality},
	// #{expirience},
	// #{college})")
	// @SelectKey(before = true, statement = "select sq_teams.nextval from dual",
	// keyProperty = "id",
	// resultType = Long.class)
	public int insert(Player player);

	// @Update("update players set playerId=#{teamName},
	// teamId=#{teamId},name=#{name},number=#{number},position=#{position},height=#{height},
	// weight=#{weight}, expirience=#{expirience},modified_on=current_timestamp
	// where playerId =
	// #{playerId}")
	public int update(Player entity);

	// @Delete("delete from players where id = #{id}")
	public int deleteById(@Param("id") Long id);

	public List<Player> getPlayersForTeam(@Param("id") Long id);

	public Team getTeamWithPlayers(@Param("teamId") String teamId);

	public List<Team> getSeasonTeamsAndPlayers(@Param("year") int year);

	public List<Player> searchPlayers(@Param("request") PlayerSearchRequest request);

	public Player getInfosForPlayer(@Param("playerId") String playerId);

	public int updateOptimistic(@Param("player") Player player,
			@Param("expectedModifiedOn") LocalDateTime expectedModifiedOn);

	public List<Player> findPlayersByName(@Param("playerName") String playerName, @Param("teamId") String teamId);

}
