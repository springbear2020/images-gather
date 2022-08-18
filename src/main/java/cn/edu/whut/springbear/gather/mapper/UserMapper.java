package cn.edu.whut.springbear.gather.mapper;

import cn.edu.whut.springbear.gather.pojo.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author Spring-_-Bear
 * @datetime 2022-08-10 23:57 Wednesday
 */
@Repository
public interface UserMapper {
    /**
     * Get the user by username and (password || email || phone)
     */
    @Select("SELECT * FROM t_user WHERE password = #{password} and (username = #{username} or phone = #{username} or email = #{username})")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "school_id", property = "schoolId"),
            @Result(column = "grade_id", property = "gradeId"),
            @Result(column = "class_id", property = "classId"),
            @Result(column = "school_id", property = "school", one = @One(select = "cn.edu.whut.springbear.gather.mapper.SchoolMapper.getSchoolNameById")),
            @Result(column = "grade_id", property = "grade", one = @One(select = "cn.edu.whut.springbear.gather.mapper.GradeMapper.getGradeNameById")),
            @Result(column = "class_id", property = "className", one = @One(select = "cn.edu.whut.springbear.gather.mapper.ClassMapper.getClassNameById"))
    })
    User getUser(@Param("username") String username, @Param("password") String password);

    /**
     * Update user information if the field is not null
     */
    int updateUser(User user);

    /**
     * Save user
     */
    @Insert("insert into t_user (username, password, phone, email, name, sex, school_id, grade_id, class_id, user_type, user_status, last_login_datetime, create_datetime) " +
            "values (#{username},#{password},#{phone},#{email},#{name},#{sex},#{schoolId},#{gradeId},#{classId},#{userType},#{userStatus},#{lastLoginDatetime},#{createDatetime})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int saveUser(User user);

    /**
     * Get the user list of class
     */
    @Select("select * from t_user where class_id = #{classId}")
    List<User> listUsersOfClass(@Param("classId") Integer classId);

    /**
     * Get user by username and email
     */
    @Select("select * from t_user where username = #{username} and email = #{email}")
    User getUserByUsernameAndEmail(@Param("username") String username, @Param("password") String password);

    /**
     * Get user by user id
     */
    @Select("select * from t_user where id = #{userId}")
    User getUserById(@Param("userId") Integer userId);
}
