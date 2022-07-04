package edu.whut.springbear.gather.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 2022-06-30 22:17 Thursday
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Upload implements Serializable {
    private static final long serialVersionUID = -4366394125299151753L;

    private Integer id;
    private Integer uploadStatus;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date uploadDateTime;
    private String localHealthUrl;
    private String localScheduleUrl;
    private String localClosedUrl;
    private String cloudHealthUrl;
    private String cloudScheduleUrl;
    private String cloudClosedUrl;
    private Integer userId;

    public static final int STATUS_UPLOADED = 0;
    public static final int STATUS_NON_UPLOAD = 1;
}
