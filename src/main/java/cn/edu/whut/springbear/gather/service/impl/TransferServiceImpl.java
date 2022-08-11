package cn.edu.whut.springbear.gather.service.impl;

import cn.edu.whut.springbear.gather.pojo.People;
import cn.edu.whut.springbear.gather.pojo.Upload;
import cn.edu.whut.springbear.gather.pojo.User;
import cn.edu.whut.springbear.gather.service.TransferService;
import cn.edu.whut.springbear.gather.util.DateUtils;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

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
}
