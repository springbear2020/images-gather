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
 * @datetime 2022-08-11 23:04 Thursday
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfiguration.class)
public class UploadMapperTest {
    @Autowired
    private UploadMapper uploadMapper;

    @Test
    public void getUploadsOfClassWithName() {
        List<Upload> uploadList = uploadMapper.listUploadsOfClassWithName(4, Upload.STATUS_COMPLETED, new Date());
        uploadList.forEach(System.out::println);
    }
}