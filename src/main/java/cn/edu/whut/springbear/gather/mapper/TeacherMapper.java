package cn.edu.whut.springbear.gather.mapper;

import cn.edu.whut.springbear.gather.pojo.Teacher;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-10 10:17 Wednesday
 */
@Repository
public interface TeacherMapper {
    /**
     * Get teacher by user id
     */
    @Select("select * from t_teacher where user_id = #{userId}")
    Teacher getTeacherByUserId(@Param("userId") Integer userId);

    /**
     * Update teacher info, including sex,phone number and email address
     */
    @Update("update t_teacher set sex = #{newSex}, phone = #{newPhone}, email = #{newEmail} where id = #{teacherId}")
    int updateTeacherInfo(@Param("newSex") String newSex, @Param("newPhone") String newPhone, @Param("newEmail") String newEmail, @Param("teacherId") Integer teacherId);
}
