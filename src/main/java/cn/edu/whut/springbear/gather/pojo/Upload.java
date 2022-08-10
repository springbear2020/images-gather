package cn.edu.whut.springbear.gather.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-08 16:41 Monday
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Upload {
    private static final long serialVersionUID = -4366394125299151753L;

    public static final int STATUS_UPLOADED = 0;
    public static final int STATUS_NOT_UPLOAD = 1;

    private Integer id;
    private Integer uploadStatus;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date uploadDatetime;
    private String localHealthUrl;
    private String localScheduleUrl;
    private String localClosedUrl;
    private String cloudHealthUrl;
    private String cloudScheduleUrl;
    private String cloudClosedUrl;
    private Integer userId;

    private Student student;
}
