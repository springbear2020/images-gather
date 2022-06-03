package edu.whut.bear.gather.service.impl;

import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import edu.whut.bear.gather.service.TransferService;
import edu.whut.bear.gather.util.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Spring-_-Bear
 * @datetime 6/3/2022 12:29 AM
 */
@Service
public class TransferServiceImpl implements TransferService {
    @Autowired
    private PropertyUtils propertyUtils;

    @Override
    public String[] getFileUploadToken(String key) {
        StringMap putPolicy = new StringMap();
        // Limit the file type uploaded by user
        putPolicy.put("mimeLimit", "image/*");
        putPolicy.put("fsizeLimit", propertyUtils.getMaxFileSize());
        String domain = propertyUtils.getDomain();
        String bucket = propertyUtils.getBucket();
        Auth auth = Auth.create(propertyUtils.getAccessKey(), propertyUtils.getSecretKey());
        String token = auth.uploadToken(bucket, key, 1800, putPolicy);
        return new String[]{domain, bucket, token};
    }
}
