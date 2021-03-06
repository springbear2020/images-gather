package edu.whut.springbear.gather.mapper;

import edu.whut.springbear.gather.pojo.Student;
import edu.whut.springbear.gather.pojo.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 2022-06-30 16:36 Thursday
 */
@Repository
public interface UserMapper {
    @Select("select * from t_user where username = #{username} and password = #{password}")
    @Results({@Result(property = "student", column = "student_id",
            javaType = Student.class, one = @One(select = "edu.whut.springbear.gather.mapper.StudentMapper.getStudentById"))
    })
    User getUserWithStudentByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    @Update("update t_user set last_login_datetime = #{newLoginDate} where id = #{userId}")
    int updateLastLoginDate(@Param("userId") Integer userId, @Param("newLoginDate") Date newLoginDate);

    @Update("update t_user set password = #{newPassword} where id = #{userId}")
    int updateUserPassword(@Param("newPassword") String newPassword, @Param("userId") Integer userId);

    /**
     * Get the user and student info though the foreign key in to user table
     *
     * @param userId Id of user
     * @return User with student info
     */
    @Select("select * from t_user where id = #{userId}")
    @Results({@Result(property = "student", column = "student_id",
            javaType = Student.class, one = @One(select = "edu.whut.springbear.gather.mapper.StudentMapper.getStudentById"))
    })
    User getUserWithStudentByUserId(@Param("userId") Integer userId);

    @Insert("insert into t_user (username, password, last_login_datetime, user_type, user_status, student_id) " +
            "values (#{username},#{password},#{last_login_datetime},#{userType},#{userStatus},#{studentId})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int saveUser(User user);

    @Update("update t_user set password = #{newPassword} where username = #{username}")
    int updatePasswordByUsername(@Param("username") String username, @Param("newPassword") String newPassword);
}
