package cn.edu.whut.springbear.gather.service.impl;

import cn.edu.whut.springbear.gather.mapper.PeopleMapper;
import cn.edu.whut.springbear.gather.pojo.People;
import cn.edu.whut.springbear.gather.service.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
