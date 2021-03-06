package edu.whut.springbear.gather.util;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Spring-_-Bear
 * @datetime 2022-07-01 18:11 Friday
 */
@Data
@Component
public class PropertyUtils {
    /**
     * Ip parse
     */
    @Value("${baidu.startIpParse}")
    private Boolean startIpParse;
    @Value("${baidu.ipParseUrl}")
    private String ipParseUrl;

    /**
     * Qiniu cloud
     */
    @Value("${qiniu.startQiniuService}")
    private Boolean startQiniuService;
    @Value("${qiniu.bucket}")
    private String bucket;
    @Value("${qiniu.cdnDomain}")
    private String cdnDomain;

    /**
     * Data view size
     */
    @Value("${gather.loginLogDataSize}")
    private Integer loginLogDataSize;
    @Value("${gather.loginLogPaginationSize}")
    private Integer loginLogPaginationSize;
    @Value("${gather.uploadDataSize}")
    private Integer uploadDataSize;
    @Value("${gather.uploadPaginationSize}")
    private Integer uploadPaginationSize;

    /**
     * Default image url
     */
    @Value("${gather.notUploadUrl}")
    private String notUploadUrl;
    @Value("${gather.notLoginUrl}")
    private String notLoginUrl;
    @Value("${gather.notChooseUrl}")
    private String notChooseUrl;
    @Value("${gather.invalidUrl}")
    private String invalidUrl;

    /**
     * Email
     */
    @Value("${email.startEmailService}")
    private Boolean startEmailService;
    @Value("${email.verifyCodeLength}")
    private Integer verifyCodeLength;
    @Value("${gather.contextPath}")
    private String contextPath;
}
