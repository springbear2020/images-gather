package cn.edu.whut.springbear.gather.service.impl;

import cn.edu.whut.springbear.gather.pojo.Student;
import cn.edu.whut.springbear.gather.pojo.Teacher;
import cn.edu.whut.springbear.gather.pojo.Upload;
import cn.edu.whut.springbear.gather.pojo.User;
import cn.edu.whut.springbear.gather.service.RecordService;
import cn.edu.whut.springbear.gather.service.TransferService;
import cn.edu.whut.springbear.gather.util.DateUtils;
import cn.edu.whut.springbear.gather.util.FileUtils;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-08 16:43 Monday
 */
@Service
@PropertySource("classpath:qiniu.properties")
public class TransferServiceImpl implements TransferService {
    @Value("${qiniu.qiniuService}")
    private boolean qiniuService;
    @Value("${qiniu.accessKey}")
    private String accessKey;
    @Value("${qiniu.secretKey}")
    private String secretKey;
    @Value("${qiniu.bucket}")
    private String bucket;
    @Value("${qiniu.cdnDomain}")
    private String cdnDomain;

    @Autowired
    private RecordService recordService;

    @Override
    public Upload saveImageFilesToDisk(User userWithStudent, String realPath, String userTodayPath, MultipartFile healthImage, MultipartFile scheduleImage, MultipartFile closedImage) {
        Student student = userWithStudent.getStudent();
        // The three images url can be access as source
        String datetimeNow = DateUtils.parseDatetimeNoHyphen(new Date());
        String healthKey = userTodayPath + student.getName() + "-" + "健康码-" + datetimeNow + ".png";
        String scheduleKey = userTodayPath + student.getName() + "-" + "行程码-" + datetimeNow + ".png";
        String closedKey = userTodayPath + student.getName() + "-" + "密接查-" + datetimeNow + ".png";

        try {
            // Save the health, schedule and closed image files to the disk
            healthImage.transferTo(new File(realPath + "/" + healthKey));
            scheduleImage.transferTo(new File(realPath + "/" + scheduleKey));
            closedImage.transferTo(new File(realPath + "/" + closedKey));
        } catch (IOException e) {
            return null;
        }
        return new Upload(null, Upload.STATUS_UPLOADED, new Date(), healthKey, scheduleKey, closedKey, "", "", "", userWithStudent.getId(), null);
    }

    @Override
    public Upload saveImagesToQiniuCloud(Upload upload, String realPath) {
        // The qiniu service don't open
        if (!qiniuService) {
            upload.setCloudHealthUrl("");
            upload.setCloudScheduleUrl("");
            upload.setCloudClosedUrl("");
            return upload;
        }

        // Three images access path
        String healthKey = upload.getLocalHealthUrl();
        String scheduleKey = upload.getLocalScheduleUrl();
        String closedKey = upload.getLocalClosedUrl();
        // Qiniu file upload service
        Auth auth = Auth.create(accessKey, secretKey);
        String uploadToken = auth.uploadToken(bucket);
        UploadManager uploadManager = new UploadManager(new Configuration(Region.region2()));
        try {
            // Upload the three files in order
            uploadManager.put(new File(realPath + "/" + healthKey), healthKey, uploadToken, null, "image/*", false);
            uploadManager.put(new File(realPath + "/" + scheduleKey), scheduleKey, uploadToken, null, "image/*", false);
            uploadManager.put(new File(realPath + "/" + closedKey), closedKey, uploadToken, null, "image/*", false);
        } catch (IOException e) {
            return upload;
        }
        upload.setCloudHealthUrl(cdnDomain + healthKey);
        upload.setCloudScheduleUrl(cdnDomain + scheduleKey);
        upload.setCloudClosedUrl(cdnDomain + closedKey);
        return upload;
    }

    @Override
    public String compressDirectory(User user, String realPath, String date) {
        // Get the class file save directory
        String srcDirectoryPath;
        String dstCompressFilePath;
        Integer userType = user.getUserType();
        if (userType == User.TYPE_TEACHER) {
            Teacher teacher = user.getTeacher();
            srcDirectoryPath = realPath + "/images-gather/" + teacher.getSchool() + "/" + teacher.getGrade() + "/" + teacher.getClassName();
        } else {
            Student student = user.getStudent();
            srcDirectoryPath = realPath + "/images-gather/" + student.getSchool() + "/" + student.getGrade() + "/" + student.getClassName();
        }
        // File full path, realPath + school + grade + class + date.zip
        dstCompressFilePath = srcDirectoryPath + "/" + date + ".zip";
        // Directory full path, realPath + school + grade + class + date
        srcDirectoryPath = srcDirectoryPath + "/" + date + "/";

        // Compress the specified directory
        if (!FileUtils.compress(srcDirectoryPath, dstCompressFilePath)) {
            return null;
        }
        return dstCompressFilePath;
    }

    @Override
    public boolean generateReadmeFile(User user, String realPath, String dateStr) {
        // Error date format string
        Date date = DateUtils.parseString(dateStr);
        if (date == null) {
            return false;
        }

        // README.txt file full path
        String className;
        String readmeFilePath;
        Integer userType = user.getUserType();
        if (userType == User.TYPE_TEACHER) {
            Teacher teacher = user.getTeacher();
            readmeFilePath = realPath + "/images-gather/" + teacher.getSchool() + "/" + teacher.getGrade() + "/" + teacher.getClassName() + "/" + dateStr + "/README.txt";
            className = teacher.getClassName();
        } else {
            Student student = user.getStudent();
            readmeFilePath = realPath + "/images-gather/" + student.getSchool() + "/" + student.getGrade() + "/" + student.getClassName() + "/" + dateStr + "/README.txt";
            className = student.getClassName();
        }

        // List[0]: not login student name list;  List[1]: not upload student name list;    List[2]: completed student name list
        List<List<String>> classUploadStudents = recordService.getClassUploadStudents(className, date);
        // Not login student list string
        List<String> notLoginStudentNames = classUploadStudents.get(0);
        StringBuilder content = new StringBuilder("未登录人员名单【" + notLoginStudentNames.size() + "】");
        for (String studentName : notLoginStudentNames) {
            content.append(studentName).append(" ");
        }
        // Not upload student list string
        List<String> notUploadStudentNames = classUploadStudents.get(1);
        content.append("\n未上传人员名单【").append(notUploadStudentNames.size()).append("】");
        for (String studentName : notUploadStudentNames) {
            content.append(studentName).append(" ");
        }
        // Upload completely student list string
        List<String> completedStudentNames = classUploadStudents.get(2);
        content.append("\n已完成人员名单【").append(completedStudentNames.size()).append("】");
        for (String studentName : completedStudentNames) {
            content.append(studentName).append(" ");
        }

        // Write string content to the new file named README.txt
        return FileUtils.writeStringContent(content.toString(), readmeFilePath);
    }
}
