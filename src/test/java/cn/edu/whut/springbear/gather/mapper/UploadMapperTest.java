package cn.edu.whut.springbear.gather.mapper;

import cn.edu.whut.springbear.gather.config.SpringConfiguration;
import cn.edu.whut.springbear.gather.pojo.Upload;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;


/**
 * @author Spring-_-Bear
 * @datetime 2022-08-09 11:17 Tuesday
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfiguration.class)
public class UploadMapperTest {
    @Autowired
    private UploadMapper uploadMapper;

    @Test
    public void getClassUploadListWithStudent() {
        List<Upload> uploadList = uploadMapper.getClassUploadListWithStudent(Upload.STATUS_UPLOADED, new Date(), "软件zy1901");
        uploadList.forEach(System.out::println);
    }
}