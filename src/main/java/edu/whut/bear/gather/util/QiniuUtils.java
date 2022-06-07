package edu.whut.bear.gather.util;

import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import edu.whut.bear.gather.pojo.Upload;
import edu.whut.bear.gather.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 6/6/2022 9:28 PM
 */
@Component
public class QiniuUtils {
    @Autowired
    private PropertyUtils propertyUtils;

    /**
     * 从七牛云平台获取图片上传 token 信息
     *
     * @param key 文件名
     * @return 七牛云验证 token
     */
    public String getImageUploadToken(String key) {
        StringMap putPolicy = new StringMap();
        // 限制文件上传类型
        putPolicy.put("mimeLimit", "image/*");
        // 限制文件上传大小
        putPolicy.put("fsizeLimit", propertyUtils.getMaxFileSize());
        // 以覆盖的方式进行上传
        putPolicy.put("scope", propertyUtils.getDomain() + ":" + key);
        Auth auth = Auth.create(propertyUtils.getAccessKey(), propertyUtils.getSecretKey());
        // 参数说明：空间名，文件名，token 有效期（10 分钟），限制策略
        return auth.uploadToken(propertyUtils.getBucket(), key, 600, putPolicy);
    }

    /**
     * 获得图片文件的格式化命名
     *
     * @param user     用户
     * @param fileType 文件类型
     * @return 文件名
     */
    public String getImageFormatFileName(User user, int fileType) {
        String fileTypeString;
        switch (fileType) {
            case Upload.HEALTH_IMAGE:
                fileTypeString = "健康码";
                break;
            case Upload.SCHEDULE_IMAGE:
                fileTypeString = "行程码";
                break;
            case Upload.CLOSED_IMAGE:
                fileTypeString = "密接查";
                break;
            default:
                return null;
        }

        // TODO 重命名文件为此格式 2022-06-03/1-李春雄-0121910870705-健康码-20220223.png
        return "develop/" + user.getClassNumber() + "-" + user.getRealName() + "-" + user.getUsername()
                + fileTypeString + "-" + "-" + DateUtils.parseDateNoHyphen(new Date()) + ".png";
    }
}
