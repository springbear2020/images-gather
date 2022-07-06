package edu.whut.springbear.gather.mapper;

import edu.whut.springbear.gather.config.SpringConfiguration;
import edu.whut.springbear.gather.pojo.Upload;
import edu.whut.springbear.gather.util.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;


/**
 * @author Spring-_-Bear
 * @datetime 2022-06-30 22:40 Thursday
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfiguration.class)
public class UploadMapperTest {
    @Autowired
    private UploadMapper uploadMapper;

    @Test
    public void saveUpload() {
        Upload upload = new Upload();
        upload.setUserId(1);
        upload.setUploadStatus(1);
        System.out.println(uploadMapper.saveUpload(upload));
    }

    @Test
    public void updateUserUploadLocalUrl() {
        Upload upload = new Upload();
        upload.setUploadStatus(Upload.STATUS_UPLOADED);
        upload.setUploadDateTime(new Date());
        upload.setLocalHealthUrl("springbear");
        upload.setLocalScheduleUrl("springbear");
        upload.setLocalClosedUrl("springbear");
        upload.setUserId(1);
        System.out.println(uploadMapper.updateUserUploadImagesUrl(upload));
    }

    @Test
    public void getUserUploadInSpecifiedDate() {
        System.out.println(uploadMapper.getUserUploadAtSpecifiedDate(1, 0, new Date()));
    }

    @Test
    public void getAllUserUploads() {
        List<Upload> uploadList = uploadMapper.getUserAllUploads(1, 0);
        uploadList.forEach(System.out::println);
    }

    @Test
    public void getClassUploadListWithStudentByStatusOnSpecifiedDay() {
        List<Upload> uploadList = uploadMapper.getClassUploadListWithStudentByStatusOnSpecifiedDay(0, DateUtils.parseStringWithHyphen("2022-07-04", new Date()), "软件zy1901");
        uploadList.forEach(System.out::println);
    }
}