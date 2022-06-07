package edu.whut.bear.gather.dao;

import edu.whut.bear.gather.pojo.Upload;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author Spring-_-Bear
 * @datetime 6/6/2022 9:15 PM
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

    @Test
    public void saveUploadBatch() {
        Upload upload1 = new Upload(1, Upload.HEALTH_IMAGE, new Date(), "whut-gather-images", "http://whut.springbear2020.cn/", "2.png");
        Upload upload2 = new Upload(1, Upload.HEALTH_IMAGE, new Date(), "whut-gather-images", "http://whut.springbear2020.cn/", "2.png");
        List<Upload> recordList = new ArrayList<>();
        recordList.add(upload1);
        recordList.add(upload2);
        int i = uploadDao.saveUploadBatch(recordList);
        System.out.println(i);
    }
}