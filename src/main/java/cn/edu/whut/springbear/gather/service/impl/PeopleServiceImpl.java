package cn.edu.whut.springbear.gather.service.impl;

import cn.edu.whut.springbear.gather.mapper.PeopleMapper;
import cn.edu.whut.springbear.gather.pojo.People;
import cn.edu.whut.springbear.gather.pojo.Upload;
import cn.edu.whut.springbear.gather.pojo.User;
import cn.edu.whut.springbear.gather.service.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-11 15:17 Thursday
 */
@Service
public class PeopleServiceImpl implements PeopleService {
    @Autowired
    private PeopleMapper peopleMapper;

    @Override
    public People queryPeople(Integer userId) {
        return peopleMapper.getPeopleByUserId(userId);
    }

    @Override
    public boolean updatePeopleInfo(String newSex, String newEmail, String newPhone, Integer id) {
        People people = new People();
        people.setId(id);
        people.setSex(newSex);
        people.setEmail(newEmail);
        people.setPhone(newPhone);
        return peopleMapper.updatePeople(people) == 1;
    }

    @Override
    public List<String> queryNotLoginNamesOfClass(String className, Date specifiedDate) {
        return peopleMapper.getNotLoginNamesOfClass(className, specifiedDate, User.TYPE_MONITOR);
    }

    @Override
    public List<String> queryNotUploadNamesOfClass(String className, Date specifiedDate) {
        return peopleMapper.getNamesOfClassByUploadStatus(className, specifiedDate, Upload.STATUS_NOT_UPLOAD);
    }

    @Override
    public List<String> queryCompletedNamesOfClass(String className, Date specifiedDate) {
        return peopleMapper.getNamesOfClassByUploadStatus(className, specifiedDate, Upload.STATUS_COMPLETED);
    }
}
