package edu.whut.bear.gather.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 6/2/2022 11:43 PM
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
}
