package edu.whut.bear.gather.util;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author Spring-_-Bear
 * @datetime 6/6/2022 2:36 PM
 */
@Data
@Component
@PropertySource("classpath:properties/qiniu.properties")
@PropertySource("classpath:properties/project.properties")
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

    /**
     * Config info of the project
     */
    @Value("${project.contextPath}")
    private String contextPath;
}
