package edu.whut.springbear.gather.service.impl;


import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import edu.whut.springbear.gather.pojo.Response;
import edu.whut.springbear.gather.pojo.Student;
import edu.whut.springbear.gather.pojo.Upload;
import edu.whut.springbear.gather.pojo.User;
import edu.whut.springbear.gather.service.TransferService;
import edu.whut.springbear.gather.util.DateUtils;
import edu.whut.springbear.gather.util.PropertyUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

/**
 * @author Spring-_-Bear
 * @datetime 2022-07-01 11:08 Friday
 */
@Service
public class TransferServiceImpl implements TransferService {
    @Resource
    private PropertyUtils propertyUtils;
    @Resource
    private UploadManager uploadManager;
    @Resource
    private Auth auth;

    @Override
    public Response judgeThreeImagesFormat(MultipartFile healthImage, MultipartFile scheduleImage, MultipartFile closedImage) {
        if (healthImage == null || !Objects.requireNonNull(healthImage.getContentType()).contains("image")) {
            return Response.warn("请选择正确的健康码图片");
        }
        if (scheduleImage == null || !Objects.requireNonNull(scheduleImage.getContentType()).contains("image")) {
            return Response.warn("请选择正确的行程码图片");
        }
        if (closedImage == null || !Objects.requireNonNull(closedImage.getContentType()).contains("image")) {
            return Response.warn("请选择正确的密接查图片");
        }
        return null;
    }

    @Override
    public String createUserTodayDirectory(User userWithStudent, String realPath) {
        // Judge the user's directory of today, if not existed, then create it
        Student student = userWithStudent.getStudent();
        String userTodayPath = "images/" + student.getSchool() + "/" + student.getCollege() + "/" +
                student.getGrade() + "/" + student.getMajor() + "/" + student.getClassName() + "/" +
                DateUtils.parseDateWithHyphen(new Date()) + "/";
        File userTodayFilePath = new File(realPath + "/" + userTodayPath);
        if (!userTodayFilePath.exists()) {
            // Create the directory
            if (!userTodayFilePath.mkdirs()) {
                return null;
            }
        }
        return userTodayPath;
    }

    @Override
    public Upload saveImageFilesToDisk(User userWithStudent, String realPath, String userTodayPath,
                                       MultipartFile healthImage, MultipartFile scheduleImage, MultipartFile closedImage) {
        Student student = userWithStudent.getStudent();
        // The three images url can be access as source
        String datetimeNow = DateUtils.parseDatetimeNoHyphen(new Date());
        String healthUrl = userTodayPath + student.getName() + "-" + "健康码-" + datetimeNow + ".png";
        String scheduleUrl = userTodayPath + student.getName() + "-" + "行程码-" + datetimeNow + ".png";
        String closedUrl = userTodayPath + student.getName() + "-" + "密接查-" + datetimeNow + ".png";

        // Save the health, schedule and closed image files to the disk
        try {
            healthImage.transferTo(new File(realPath + "/" + healthUrl));
            scheduleImage.transferTo(new File(realPath + "/" + scheduleUrl));
            closedImage.transferTo(new File(realPath + "/" + closedUrl));
        } catch (IOException e) {
            return null;
        }
        return new Upload(null, Upload.STATUS_UPLOADED, new Date(), healthUrl, scheduleUrl, closedUrl, "", "", "", userWithStudent.getId());
    }

    @Override
    public Upload saveImagesToCloud(Upload upload, String realPath) {
        // Three images access path
        String healthUrl = upload.getLocalHealthUrl();
        String scheduleUrl = upload.getLocalScheduleUrl();
        String closedUrl = upload.getLocalClosedUrl();
        // Cnd domain from Qiniu server
        String cdnDomain = propertyUtils.getCdnDomain();
        String bucket = propertyUtils.getBucket();
        // Qiniu file upload service
        String uploadToken = auth.uploadToken(bucket);
        try {
            // Upload the three files in order
            uploadManager.put(new File(realPath + "/" + healthUrl), healthUrl, uploadToken, null, "image/*", false);
            upload.setCloudHealthUrl(cdnDomain + healthUrl);
            uploadManager.put(new File(realPath + "/" + scheduleUrl), scheduleUrl, uploadToken, null, "image/*", false);
            upload.setCloudScheduleUrl(cdnDomain + scheduleUrl);
            uploadManager.put(new File(realPath + "/" + closedUrl), closedUrl, uploadToken, null, "image/*", false);
            upload.setCloudClosedUrl(cdnDomain + closedUrl);
        } catch (IOException e) {
            // Ignore Something wrong with Qiniu server save the files
        }
        return upload;
    }
}
