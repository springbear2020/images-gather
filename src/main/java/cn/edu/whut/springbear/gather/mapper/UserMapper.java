package cn.edu.whut.springbear.gather.mapper;


import cn.edu.whut.springbear.gather.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-08 09:58 Monday
 */
@Repository
public interface UserMapper {
    /**
     * Get the user by username and password
     */
    @Select("select * from t_user where username = #{username} and password = #{password}")
    User getUserByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    /**
     * Update the user's login datetime
     */
    @Update("update t_user set last_login_datetime = #{newLoginDate} where id = #{userId}")
    int updateLastLoginDate(@Param("userId") Integer userId, @Param("newLoginDate") Date newLoginDate);

    /**
     * Update user login password by username
     */
    @Update("update t_user set password = #{newPassword} where username = #{username}")
    int updatePasswordByUsername(@Param("username") String username, @Param("newPassword") String newPassword);

    /**
     * Get user by username and email address
     */
    @Select("select * from t_user where username = #{username} and email = #{email}")
    User getUserByUsernameAndEmail(@Param("username") String username, @Param("email") String email);

    /**
     * Update the user info, including user email and phone number
     */
    @Update("update t_user set phone = #{newPhone}, email = #{newEmail} where id = #{userId}")
    int updateUserInfo(@Param("newPhone") String newPhone, @Param("newEmail") String newEmail, @Param("userId") Integer userId);
}
