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
    @Value("${qiniu.startService}")
    private Boolean startService;
    @Value("${qiniu.accessKey}")
    private String accessKey;
    @Value("${qiniu.secretKey}")
    private String secretKey;
    @Value("${qiniu.bucket}")
    private String bucket;
    @Value("${qiniu.cdnDomain}")
    private String cdnDomain;
}
