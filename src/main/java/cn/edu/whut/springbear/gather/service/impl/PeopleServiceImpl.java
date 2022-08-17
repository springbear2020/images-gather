package cn.edu.whut.springbear.gather.service.impl;

import cn.edu.whut.springbear.gather.mapper.PeopleMapper;
import cn.edu.whut.springbear.gather.pojo.People;
import cn.edu.whut.springbear.gather.pojo.User;
import cn.edu.whut.springbear.gather.service.PeopleService;
import cn.edu.whut.springbear.gather.service.UserService;
import cn.edu.whut.springbear.gather.util.DateUtils;
import cn.edu.whut.springbear.gather.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


/**
 * @author Spring-_-Bear
 * @datetime 2022-08-11 15:17 Thursday
 */
@Service
public class PeopleServiceImpl implements PeopleService {
    @Autowired
    private PeopleMapper peopleMapper;
    @Autowired
    private UserService userService;

    @Override
    public People queryPeople(Integer userId) {
        return peopleMapper.getPeopleByUserId(userId);
    }

    @Override
    public boolean updatePeopleInfo(String newSex, String newEmail, String newPhone, Integer id) {
        return peopleMapper.updatePeople(new People(id, newSex, newPhone, newEmail)) == 1;
    }

    @Override
    public boolean saveHeadTeacher(People people) {
        // Register user TODO Transactional?
        User user = new User(people.getNumber(), null, DateUtils.parseString("1970-01-01"), people.getPhone(), people.getEmail(), User.STATUS_NORMAL, User.TYPE_GRADE_TEACHER, new Date());
        user.setPassword(MD5Utils.md5Encrypt(people.getNumber()));
        if (!userService.saveUser(user)) {
            return false;
        }
        people.setUserId(user.getId());
        people.setCreateDatetime(new Date());
        return peopleMapper.savePeople(people) == 1;
    }
}
