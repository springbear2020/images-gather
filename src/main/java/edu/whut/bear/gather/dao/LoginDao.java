package edu.whut.bear.gather.dao;

import edu.whut.bear.gather.pojo.Login;
import org.springframework.stereotype.Repository;

/**
 * @author Spring-_-Bear
 * @datetime 6/6/2022 8:08 PM
 */
@Repository
public interface LoginDao {
    /**
     * 保存用户登录记录
     *
     * @param login 登录记录
     * @return 1 - 保存成功
     */
    int saveLogin(Login login);
}
