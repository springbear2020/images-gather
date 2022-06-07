package edu.whut.bear.gather.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 6/6/2022 9:11 PM
 */
@Data
@Component
public class Upload {
    private Integer id;
    private Integer userId;
    private Integer fileType;
    private Date uploadTime;
    private String bucket;
    private String domain;
    private String key;

    public static final int HEALTH_IMAGE = 1;
    public static final int SCHEDULE_IMAGE = 2;
    public static final int CLOSED_IMAGE = 3;

    public Upload() {
    }

    public Upload(Integer userId, Integer fileType, Date uploadTime, String bucket, String domain, String key) {
        this.userId = userId;
        this.fileType = fileType;
        this.uploadTime = uploadTime;
        this.bucket = bucket;
        this.domain = domain;
        this.key = key;
    }
}
