package edu.whut.bear.gather.service.impl;

import edu.whut.bear.gather.dao.LoginDao;
import edu.whut.bear.gather.pojo.Login;
import edu.whut.bear.gather.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Spring-_-Bear
 * @datetime 6/6/2022 8:11 PM
 */
@Service
public class RecordServiceImpl implements RecordService {
    @Autowired
    private LoginDao loginDao;

    @Override
    public boolean saveLogin(Login login) {
        return loginDao.saveLogin(login) == 1;
    }
}
