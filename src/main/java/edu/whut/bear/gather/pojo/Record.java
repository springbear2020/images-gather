package edu.whut.bear.gather.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 6/6/2022 8:33 PM
 */
@Data
@Component
public class Record {
    private Integer id;
    private Integer userId;
    private String studentNumber;
    private String realName;
    private Integer classNumber;
    private String className;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date uploadDate;
    private Integer healthUploadId;
    private Integer scheduleUploadId;
    private Integer closedUploadId;
    private String healthImageUrl;
    private String scheduleImageUrl;
    private String closedImageUrl;

    private int uploaded;

    public static final int YES = 0;
    public static final int NO = 1;
    public static final int UN_UPLOADED = -1;

    public Record() {
    }

    public Record(Integer userId, String studentNumber, String realName, Integer classNumber, String className, Integer healthUploadId, Integer scheduleUploadId, Integer closedUploadId, Date uploadDate, String healthImageUrl, String scheduleImageUrl, String closedImageUrl) {
        this.id = id;
        this.userId = userId;
        this.studentNumber = studentNumber;
        this.realName = realName;
        this.classNumber = classNumber;
        this.className = className;
        this.healthUploadId = healthUploadId;
        this.scheduleUploadId = scheduleUploadId;
        this.closedUploadId = closedUploadId;
        this.uploadDate = uploadDate;
        this.healthImageUrl = healthImageUrl;
        this.scheduleImageUrl = scheduleImageUrl;
        this.closedImageUrl = closedImageUrl;
    }
}

