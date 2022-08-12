package cn.edu.whut.springbear.gather.service.impl;

import cn.edu.whut.springbear.gather.pojo.People;
import cn.edu.whut.springbear.gather.pojo.Upload;
import cn.edu.whut.springbear.gather.pojo.User;
import cn.edu.whut.springbear.gather.service.PeopleService;
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
 * @datetime 2022-08-11 16:21 Thursday
 */
@Service
@PropertySource("classpath:properties/qiniu.properties")
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
    private PeopleService peopleService;

    @Override
    public Upload saveImageFilesToDisk(User user, String realPath, String userTodayPath, MultipartFile healthImage, MultipartFile scheduleImage, MultipartFile closedImage) {
        People people = user.getPeople();
        String datetimeNow = DateUtils.parseDatetimeNoHyphen(new Date());
        // images-gather/school/grade/class/name-健康码-20220811162311.png
        String healthKey = userTodayPath + people.getName() + "-" + "健康码-" + datetimeNow + ".png";
        String scheduleKey = userTodayPath + people.getName() + "-" + "行程码-" + datetimeNow + ".png";
        String closedKey = userTodayPath + people.getName() + "-" + "密接查-" + datetimeNow + ".png";

        try {
            // Save the health, schedule and closed image files to the disk
            healthImage.transferTo(new File(realPath + "/" + healthKey));
            scheduleImage.transferTo(new File(realPath + "/" + scheduleKey));
            closedImage.transferTo(new File(realPath + "/" + closedKey));
        } catch (IOException e) {
            return null;
        }
        return new Upload(Upload.STATUS_COMPLETED, new Date(), healthKey, scheduleKey, closedKey, user.getId());
    }

    @Override
    public Upload pushImagesToQiniu(Upload upload, String realPath) {
        if (!qiniuService) {
            return upload;
        }

        // Three images' access path
        String healthKey = upload.getLocalHealthUrl();
        String scheduleKey = upload.getLocalScheduleUrl();
        String closedKey = upload.getLocalClosedUrl();

        // Qiniu file upload service
        Auth auth = Auth.create(accessKey, secretKey);
        String uploadToken = auth.uploadToken(bucket);
        UploadManager uploadManager = new UploadManager(new Configuration(Region.region2()));

        try {
            // Push the three files to qiniu
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
    public boolean createReadmeFile(String realPath, String dateStr, People people) {
        // Error parameter
        Date date = DateUtils.parseString(dateStr);
        if (date == null) {
            return false;
        }
        // e.g: 武汉理工大学/2019/软件zy1901
        String classInfo = people.getSchool() + "/" + people.getGrade() + "/" + people.getClassName();
        // e.g: E:\images-gather\target\images-gather-1.0-SNAPSHOT\images-gather/武汉理工大学/2019/软件zy1901/2022-08-12
        String fileSaveDirectory = realPath + "/images-gather/" + classInfo + "/" + dateStr;
        // Create the README.txt file save directory
        if (!FileUtils.createDirectory(fileSaveDirectory)) {
            return false;
        }

        // e.g: E:\images-gather\target\images-gather-1.0-SNAPSHOT\images-gather/school/grade/class/2022-08-12/README.txt
        String readmeFilePath = fileSaveDirectory + "/README.txt";
        // e.g: 武汉理工大学/2019/软件zy1901/2022-08-12 【两码一查】完成情况如下：
        StringBuilder content = new StringBuilder(classInfo + "/" + dateStr + " 【两码一查】完成情况如下：");
        // Not login people name list string
        List<String> notLoginNames = peopleService.queryNotLoginNamesOfClass(people.getClassName(), date);
        content.append("\n\n    未登录人员名单【").append(notLoginNames.size()).append("】");
        for (String name : notLoginNames) {
            content.append(name).append(" ");
        }
        // Not upload people name list string
        List<String> notUploadNames = peopleService.queryNotUploadNamesOfClass(people.getClassName(), date);
        content.append("\n    未上传人员名单【").append(notUploadNames.size()).append("】");
        for (String name : notUploadNames) {
            content.append(name).append(" ");
        }
        // Upload completely people name list string
        List<String> completedNames = peopleService.queryCompletedNamesOfClass(people.getClassName(), date);
        content.append("\n    已完成人员名单【").append(completedNames.size()).append("】");
        for (String name : completedNames) {
            content.append(name).append(" ");
        }
        content.append("\n\n统计时间：").append(DateUtils.parseDatetime(new Date()));
        return FileUtils.writeStringContent(content.toString(), readmeFilePath);
    }

    @Override
    public String compressDirectory(String realPath, String dateStr, People people) {
        // e.g: E:\images-gather\target\images-gather-1.0-SNAPSHOT\images-gather/武汉理工大学/2019/软件zy1901/2022-08-12
        String fileSaveDirectory = realPath + "/images-gather/" + people.getSchool() + "/" + people.getGrade() + "/" + people.getClassName() + "/" + dateStr;
        // e.g: E:\images-gather\target\images-gather-1.0-SNAPSHOT\images-gather/武汉理工大学/2019/软件zy1901/2022-08-12.zip
        String zipFilePath = fileSaveDirectory + ".zip";
        return FileUtils.compressDirectory(fileSaveDirectory, zipFilePath) ? zipFilePath : null;
    }
}
