package edu.whut.bear.gather.util;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author Spring-_-Bear
 * @datetime 6/3/2022 12:26 AM
 */
@Data
@Component
@PropertySource("classpath:gather.properties")
public class PropertyUtils {
    /**
     * Qiniu config data
     */
    @Value("${qiniu.accessKey}")
    private String accessKey;
    @Value("${qiniu.secretKey}")
    private String secretKey;
    @Value("${qiniu.bucket}")
    private String bucket;
    @Value("${qiniu.domain}")
    private String domain;
    @Value("${qiniu.maxFileSize}")
    private Integer maxFileSize;

    @Value("${gather.domain}")
    private String contextUrl;
}
