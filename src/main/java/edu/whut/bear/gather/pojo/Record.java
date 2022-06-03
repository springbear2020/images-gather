package edu.whut.bear.gather.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 6/3/2022 12:02 PM
 */
@Data
@Component
public class Record {
    private Integer id;
    private Integer userId;
    private Integer classNumber;
    private String className;
    private Integer healthUploadId;
    private Integer scheduleUploadId;
    private Integer closedUploadId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date uploadDate;
    private String healthImageUrl;
    private String scheduleImageUrl;
    private String closedImageUrl;

    public Record() {
    }

    public Record(Integer id, Integer userId, Integer classNumber, String className, Integer healthUploadId, Integer scheduleUploadId, Integer closedUploadId, Date uploadDate, String healthImageUrl, String scheduleImageUrl, String closedImageUrl) {
        this.id = id;
        this.userId = userId;
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
