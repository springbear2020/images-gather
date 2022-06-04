package edu.whut.bear.gather.dao;

import edu.whut.bear.gather.pojo.Upload;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;


/**
 * @author Spring-_-Bear
 * @datetime 6/3/2022 12:12 AM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class UploadDaoTest {
    @Autowired
    private UploadDao uploadDao;

    @Test
    public void saveUpload() {
        System.out.println(uploadDao.saveUpload(new Upload(1, Upload.HEALTH_IMAGE, new Date(), "whut-gather-images", "http://whut.springbear2020.cn/", "2.png")));
    }
}