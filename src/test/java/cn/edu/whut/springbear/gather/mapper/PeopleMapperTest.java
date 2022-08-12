package cn.edu.whut.springbear.gather.mapper;

import cn.edu.whut.springbear.gather.config.SpringConfiguration;
import cn.edu.whut.springbear.gather.pojo.Upload;
import cn.edu.whut.springbear.gather.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;


/**
 * @author Spring-_-Bear
 * @datetime 2022-08-11 22:42 Thursday
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfiguration.class)
public class PeopleMapperTest {
    @Autowired
    private PeopleMapper peopleMapper;

    @Test
    public void getNotLoginNamesOfClass() {
        List<String> strings = peopleMapper.getNotLoginNamesOfClass("软件zy1901", new Date(), User.TYPE_MONITOR);
        System.out.println(strings.size());
        System.out.println(strings);
    }

    @Test
    public void getNotUploadNamesOfClass() {
        System.out.println(peopleMapper.getNamesOfClassByUploadStatus("软件zy1901", new Date(), Upload.STATUS_NOT_UPLOAD));
    }
}