package edu.whut.springbear.gather.service.impl;

import edu.whut.springbear.gather.mapper.LoginLogMapper;
import edu.whut.springbear.gather.mapper.UploadMapper;
import edu.whut.springbear.gather.pojo.LoginLog;
import edu.whut.springbear.gather.pojo.Upload;
import edu.whut.springbear.gather.service.RecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
}
