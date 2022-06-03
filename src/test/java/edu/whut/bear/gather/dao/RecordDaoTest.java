package edu.whut.bear.gather.dao;

import edu.whut.bear.gather.pojo.Record;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;


/**
 * @author Spring-_-Bear
 * @datetime 6/3/2022 12:36 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class RecordDaoTest {
    @Autowired
    private RecordDao recordDao;

    @Test
    public void saveRecord() {
        System.out.println(recordDao.saveRecord(new Record(null, 1, 1, "软件zy1901", -1, -1, -1, new Date(), "", "", "")));
    }
}