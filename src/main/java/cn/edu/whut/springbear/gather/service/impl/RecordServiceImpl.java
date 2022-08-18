package cn.edu.whut.springbear.gather.service.impl;

import cn.edu.whut.springbear.gather.mapper.EmailMapper;
import cn.edu.whut.springbear.gather.mapper.LoginMapper;
import cn.edu.whut.springbear.gather.mapper.UploadMapper;
import cn.edu.whut.springbear.gather.pojo.Email;
import cn.edu.whut.springbear.gather.pojo.Login;
import cn.edu.whut.springbear.gather.pojo.Upload;
import cn.edu.whut.springbear.gather.service.RecordService;
import cn.edu.whut.springbear.gather.util.WebUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-11 00:10 Thursday
 */
@Service
@PropertySource("classpath:baidu.properties")
public class RecordServiceImpl implements RecordService {
    @Value("${baidu.ipService}")
    private Boolean ipService;
    @Value("${baidu.parseUrl}")
    private String parseUrl;

    public static final int PAGE_DATA_ROWS = 10;
    public static final int PAGE_NAVIGATIONS = 7;

    @Autowired
    private LoginMapper loginMapper;
    @Autowired
    private UploadMapper uploadMapper;
    @Autowired
    private EmailMapper emailMapper;

    @Override
    public boolean saveLoginLog(String ip, Integer userId) {
        String location = "未知地点";
        // Parse the location of the ip address from the baidu map api
        if (ipService && !"127.0.0.1".equals(ip)) {
            location = WebUtils.parseIpLocation(parseUrl + ip);
        }
        return loginMapper.saveLoginLog(new Login(ip, location, new Date(), userId)) == 1;
    }

    @Override
    public boolean createUserUploadToday(Integer userId) {
        String empty = "";
        return uploadMapper.saveUpload(new Upload(Upload.STATUS_NOT_UPLOAD, new Date(), empty, empty, empty, empty, empty, empty, userId, new Date())) == 1;
    }

    @Override
    public boolean saveEmailLog(Email email) {
        return emailMapper.saveEmailLog(email) == 1;
    }

    @Override
    public Upload getUserUpload(Integer userId, Date date, Integer uploadStatus) {
        return uploadMapper.getUpload(userId, uploadStatus, date);
    }

    @Override
    public boolean updateUpload(Upload upload) {
        return uploadMapper.updateUpload(upload) == 1;
    }

    @Override
    public PageInfo<Login> listUserLoginLogPageData(Integer userId, Integer pageNum) {
        PageHelper.startPage(pageNum, PAGE_DATA_ROWS);
        List<Login> loginList = loginMapper.listLoginLogsOfUser(userId);
        return new PageInfo<>(loginList, PAGE_NAVIGATIONS);
    }

    @Override
    public PageInfo<Upload> listUserUploadPageData(Integer userId, Integer uploadStatus, Integer pageNum) {
        PageHelper.startPage(pageNum, PAGE_DATA_ROWS);
        List<Upload> uploadList = uploadMapper.listUploadsOfUser(userId, uploadStatus);
        return new PageInfo<>(uploadList, PAGE_NAVIGATIONS);
    }

    @Override
    public List<Upload> listUploadsOfClass(Integer classId, Integer uploadStatus, Date date) {
        return uploadMapper.listUploadsOfClassWithName(classId, uploadStatus, date);
    }

    @Override
    public Login getUserLatestLoginLog(Integer userId) {
        return loginMapper.getUserLatestLoginLog(userId);
    }
}
