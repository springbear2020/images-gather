package edu.whut.bear.gather.dao;

import edu.whut.bear.gather.pojo.Record;
import edu.whut.bear.gather.service.RecordService;
import edu.whut.bear.gather.util.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;


/**
 * @author Spring-_-Bear
 * @datetime 6/3/2022 12:36 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class RecordDaoTest {
    @Autowired
    private RecordDao recordDao;
    @Autowired
    private RecordService recordService;

    @Test
    public void saveRecord() {
        System.out.println(recordDao.saveRecord(new Record(null, 1, "1", "李春雄", 1, "软件zy1901", -1, -1, -1, new Date(), "", "", "")));
    }

    @Test
    public void getUserRecordByDate() {
        System.out.println(recordDao.getUserRecordByDate(108, new Date()));
    }

    @Test
    public void updateRecordState() {
        Record record = new Record();
        record.setId(31);
        record.setHealthUploadId(2);
        record.setHealthImageUrl("2");

        record.setScheduleUploadId(2);
        record.setScheduleImageUrl("2");

        record.setClosedUploadId(2);
        record.setClosedImageUrl("2");
        System.out.println(recordDao.updateRecordState(record));
    }


    @Test
    public void getAdminClassRecordByDate() {
        System.out.println(recordService.processRecordList(4, new Date()));
    }

    @Test
    public void getLoginByNotUploaded() {
        System.out.println(recordDao.getLoginByNotUploaded(4, new Date()));
    }

    @Test
    public void getRecordUploaded() {
        System.out.println(recordDao.getRecordUploaded(4, DateUtils.parseString("2022-06-04")));
    }
}