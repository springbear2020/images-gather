package edu.whut.springbear.gather.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.whut.springbear.gather.mapper.LoginLogMapper;
import edu.whut.springbear.gather.mapper.UploadMapper;
import edu.whut.springbear.gather.pojo.LoginLog;
import edu.whut.springbear.gather.pojo.Upload;
import edu.whut.springbear.gather.service.RecordService;
import edu.whut.springbear.gather.util.PropertyUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author Spring-_-Bear
 * @datetime 2022-06-30 21:56 Thursday
 */
@Service
public class RecordServiceImpl implements RecordService {
    @Resource
    private LoginLogMapper loginLogMapper;
    @Resource
    private UploadMapper uploadMapper;
    @Resource
    private PropertyUtils propertyUtils;

    @Override
    public boolean saveLoginLog(LoginLog loginLog) {
        return loginLogMapper.saveLoginLog(loginLog) == 1;
    }

    @Override
    public boolean saveUserUploadRecord(Upload upload) {
        return uploadMapper.saveUpload(upload) == 1;
    }

    @Override
    public boolean updateImagesAccessUrl(Upload upload) {
        return uploadMapper.updateUserUploadImagesUrl(upload) == 1;
    }

    @Override
    public Upload getUserUploadInSpecifiedDate(Integer userId, Integer uploadStatus, Date specifiedDate) {
        return uploadMapper.getUserUploadAtSpecifiedDate(userId, uploadStatus, specifiedDate);
    }

    @Override
    public String[] getThreeImagesAccessUrl(String contextPath, Upload upload) {
        String cloudHealthUrl = upload.getCloudHealthUrl();
        String cloudScheduleUrl = upload.getCloudScheduleUrl();
        String cloudClosedUrl = upload.getCloudClosedUrl();
        // If has invalid url of the cloud then return the local access url of images
        if (cloudHealthUrl == null || cloudHealthUrl.length() <= 0 ||
                cloudScheduleUrl == null || cloudScheduleUrl.length() <= 0 ||
                cloudClosedUrl == null || cloudClosedUrl.length() <= 0) {
            return new String[]{contextPath + upload.getLocalHealthUrl(),
                    contextPath + upload.getLocalScheduleUrl(), contextPath + upload.getLocalClosedUrl()};
        }
        return new String[]{cloudHealthUrl, cloudScheduleUrl, cloudClosedUrl};
    }

    @Override
    public PageInfo<LoginLog> getUserLoginPageData(Integer userId, Integer pageNum) {
        PageHelper.startPage(pageNum, propertyUtils.getLoginLogDataSize());
        List<LoginLog> loginList = loginLogMapper.getUserLoginLog(userId);
        return new PageInfo<>(loginList, propertyUtils.getLoginLogPaginationSize());
    }

    @Override
    public PageInfo<Upload> getUserUploadHistory(Integer userId, Integer uploadStatus, Integer pageNum) {
        PageHelper.startPage(pageNum, propertyUtils.getUploadDataSize());
        List<Upload> uploadList = uploadMapper.getUserAllUploads(userId, uploadStatus);
        return new PageInfo<>(uploadList, propertyUtils.getUploadPaginationSize());
    }

    @Override
    public List<Upload> getClassUploadListWithStudentOnDayByStatus(Integer uploadStatus, Date specifiedDate, String className) {
        return uploadMapper.getClassUploadListWithStudentByStatusOnSpecifiedDay(uploadStatus, specifiedDate, className);
    }
}
