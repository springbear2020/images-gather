package cn.edu.whut.springbear.gather.mapper;

import cn.edu.whut.springbear.gather.pojo.People;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;


/**
 * @author Spring-_-Bear
 * @datetime 2022-08-11 15:15 Thursday
 */
@Repository
public interface PeopleMapper {
    /**
     * TODO table?
     * Get people info by the user id
     */
    @Select("select t_people.*, t_class.class_name, t_grade.grade, t_school.school " +
            "from t_people, t_class, t_grade, t_school " +
            "where user_id = #{userId} and class_id = t_class.id and grade_id = t_grade.id and school_id = t_school.id")
    People getPeopleByUserId(@Param("userId") Integer userId);

    /**
     * Update the people info if it not null
     */
    int updatePeople(People people);
}
