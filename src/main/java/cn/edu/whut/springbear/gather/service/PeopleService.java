package cn.edu.whut.springbear.gather.service;

import cn.edu.whut.springbear.gather.pojo.People;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-11 15:16 Thursday
 */
public interface PeopleService {
    /**
     * Get people info by user id
     */
    People queryPeople(Integer userId);
}