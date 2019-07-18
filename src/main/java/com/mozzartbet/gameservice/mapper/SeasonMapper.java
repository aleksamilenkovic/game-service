package com.mozzartbet.gameservice.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import com.mozzartbet.gameservice.domain.Season;

@Mapper
public interface SeasonMapper extends BaseMapper<Season> {
  @Select("select count(*) from basketball_seasons")
  public long count();

  @Select("select id,created_on createdOn,modified_on modifiedOn,season_year seasonYear from basketball_seasons where id=#{id}")
  public Season getById(Long id);

  @Select("select id ,created_on createdOn,modified_on modifiedOn,season_year seasonYear from basketball_seasons where season_year=#{seasonYear}")
  public Season getByYear(int seasonYear);

  @Insert("insert into basketball_seasons(id,created_on,modified_on,season_year)"
      + "   values(#{id},current_timestamp,current_timestamp,#{seasonYear})")
  @SelectKey(before = true, statement = "select sq_seasons.nextval from dual", keyProperty = "id",
      resultType = Long.class)
  public int insert(Season entity);

  @Update("update basketball_seasons set season_year=#{seasonYear}, modified_on=current_timestamp where id=#{id}")
  public int update(Season entity);

  @Delete("delete from basketball_seasons where id = #{id}")
  public int deleteById(Long id);
}
