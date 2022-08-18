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
 * @datetime 2022-08-13 09:06 Saturday
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfiguration.class)
public class ClassMapperTest {
    @Autowired
    private ClassMapper classMapper;

    @Test
    public void getNotLoginNamesOfClass() {
        List<String> strings = classMapper.listNotLoginNamesOfClass(4, new Date(), User.TYPE_MONITOR);
        System.out.println(strings.size());
        System.out.println(strings);
    }

    @Test
    public void getNotUploadNamesOfClass() {
        System.out.println(classMapper.listUploadNamesOfClass(4, new Date(), Upload.STATUS_NOT_UPLOAD));
    }

    @Test
    public void getClassNameById() {
        System.out.println(classMapper.getClassNameById(1));
    }
}
