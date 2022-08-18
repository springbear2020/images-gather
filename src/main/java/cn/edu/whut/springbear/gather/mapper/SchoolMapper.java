package cn.edu.whut.springbear.gather.mapper;

import cn.edu.whut.springbear.gather.pojo.School;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-14 08:31 Sunday
 */
@Repository
public interface SchoolMapper {
    /**
     * Save school
     */
    @Insert("insert into t_school(school) values (#{school})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int saveSchool(School school);

    /**
     * Get school by name
     */
    @Select("select * from  t_school where school = #{schoolName}")
    School getSchool(@Param("schoolName") String schoolName);

    /**
     * Get all schools
     */
    @Select("select * from t_school")
    List<School> listSchools();

    /**
     * Get school name by id
     */
    @Select("select school from t_school where id = #{schoolId}")
    String getSchoolNameById(@Param("schoolId") Integer schoolId);
}
