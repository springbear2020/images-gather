package edu.whut.bear.gather.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 6/2/2022 10:01 PM
 */
@Data
@Component
public class User {
    private Integer id;
    private String username;
    private String password;
    private String realName;
    private Integer classNumber;
    private String className;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date lastRecordCreateDate;
    private Integer userType;

    public static final int COMMON = 0;
    public static final int ADMIN = 1;
}
