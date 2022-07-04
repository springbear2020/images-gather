package edu.whut.springbear.gather.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 2022-06-30 15:35 Thursday
 */
@Data
public class User implements Serializable {
    private static final long serialVersionUID = 4462486847606240343L;

    private Integer id;
    private String username;
    private String password;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date lastLoginDate;
    private Integer userType;
    private Integer userStatus;
    private Integer studentId;

    private Student student;

    public static final int STATUS_NORMAL = 0;
    public static final int STATUS_ABNORMAL = 1;

    public static final int TYPE_USER = 0;
    public static final int TYPE_ADMIN = 1;
}