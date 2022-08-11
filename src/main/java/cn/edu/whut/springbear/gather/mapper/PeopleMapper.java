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
     * Get people info by the user id
     */
    @Select("select * from t_people where user_id = #{userId}")
    People getPeopleByUserId(@Param("userId") Integer userId);
}
