package cn.edu.whut.springbear.gather.mapper;

import cn.edu.whut.springbear.gather.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-10 23:57 Wednesday
 */
@Repository
public interface UserMapper {
    /**
     * Get the user by username and password
     */
    @Select("SELECT * FROM t_user WHERE password = #{password} and (phone = #{username} or username = #{username} or email = #{username})")
    User queryUser(@Param("username") String username, @Param("password") String password);

    /**
     * Update user info if it not empty,
     * including password, phone, email and lastLoginDatetime
     */
    int updateUserById(User user);

    /**
     * Get user by username and email address
     */
    @Select("select * from t_user where username = #{username} and email = #{email}")
    User getUserByUsernameAndEmail(@Param("username") String username, @Param("email") String email);

    /**
     * Save user and return the generated auto increment key value
     */
    @Insert("insert into t_user(username, password, phone, email, last_login_datetime, user_type, user_status) values (#{username},#{password},#{phone},#{email},#{lastLoginDatetime},#{userType},#{userStatus})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int saveUser(User user);
}
