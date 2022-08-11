package cn.edu.whut.springbear.gather.service.impl;

import cn.edu.whut.springbear.gather.mapper.EmailLogMapper;
import cn.edu.whut.springbear.gather.mapper.LoginLogMapper;
import cn.edu.whut.springbear.gather.mapper.UploadMapper;
import cn.edu.whut.springbear.gather.pojo.EmailLog;
import cn.edu.whut.springbear.gather.pojo.LoginLog;
import cn.edu.whut.springbear.gather.pojo.Upload;
import cn.edu.whut.springbear.gather.service.RecordService;
import cn.edu.whut.springbear.gather.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-11 00:10 Thursday
 */
@Service
@PropertySource("classpath:properties/baidu.properties")
public class RecordServiceImpl implements RecordService {
    @Value("${baidu.ipService}")
    private Boolean ipService;
    @Value("${baidu.parseUrl}")
    private String parseUrl;

    @Autowired
    private LoginLogMapper loginLogMapper;
    @Autowired
    private UploadMapper uploadMapper;
    @Autowired
    private EmailLogMapper emailLogMapper;

    @Override
    public boolean saveLoginLog(String ip, Integer userId) {
        String location = "未知地点";
        // Parse the location of the ip address from the baidu map api
        if (ipService && !"127.0.0.1".equals(ip)) {
            location = WebUtils.parseIpLocation(parseUrl + ip);
        }
        LoginLog loginLog = new LoginLog(ip, location, new Date(), userId);
        return loginLogMapper.saveLoginLog(loginLog) == 1;
    }

    @Override
    public boolean createStudentUploadToday(Integer userId) {
        String unUploadUrl = "static/img/notUpload.png";
        Upload upload = new Upload(Upload.STATUS_NOT_UPLOAD, new Date(), unUploadUrl, unUploadUrl, unUploadUrl, unUploadUrl, unUploadUrl, unUploadUrl, userId);
        return uploadMapper.saveUpload(upload) == 1;
    }

    @Override
    public boolean saveEmailLog(EmailLog emailLog) {
        return emailLogMapper.saveEmailLog(emailLog) == 1;
    }

    @Override
    public Upload getStudentUpload(Integer userId, Date date, Integer uploadStatus) {
        return uploadMapper.getUploadOfUserFilterByStatusAndDate(userId, uploadStatus, date);
    }
}
