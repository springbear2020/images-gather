package cn.edu.whut.springbear.gather.mapper;

import cn.edu.whut.springbear.gather.pojo.User;
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
    @Select("select * from t_user where username = #{username} and password = #{password}")
    User getUserByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

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
}
