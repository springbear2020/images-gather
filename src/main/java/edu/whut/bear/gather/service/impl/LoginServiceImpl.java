package edu.whut.bear.gather.service.impl;

import edu.whut.bear.gather.dao.LoginDao;
import edu.whut.bear.gather.pojo.Login;
import edu.whut.bear.gather.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Spring-_-Bear
 * @datetime 6/3/2022 12:02 AM
 */
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private LoginDao loginDao;

    @Override
    public boolean saveLogin(Login login) {
        return loginDao.saveLogin(login) == 1;
    }
}
