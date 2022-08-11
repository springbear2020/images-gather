package cn.edu.whut.springbear.gather.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-11 00:50 Thursday
 */
@Data
@NoArgsConstructor
public class Upload {
    private static final long serialVersionUID = -4366394125299151753L;

    public static final int STATUS_COMPLETED = 0;
    public static final int STATUS_NOT_UPLOAD = 1;

    private Integer id;
    private Integer uploadStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date uploadDatetime;
    private String localHealthUrl;
    private String localScheduleUrl;
    private String localClosedUrl;
    private String cloudHealthUrl;
    private String cloudScheduleUrl;
    private String cloudClosedUrl;
    private Integer userId;

    public Upload(Integer uploadStatus, Date uploadDatetime, String localHealthUrl, String localScheduleUrl, String localClosedUrl, Integer userId) {
        this.uploadStatus = uploadStatus;
        this.uploadDatetime = uploadDatetime;
        this.localHealthUrl = localHealthUrl;
        this.localScheduleUrl = localScheduleUrl;
        this.localClosedUrl = localClosedUrl;
        this.userId = userId;
    }
}