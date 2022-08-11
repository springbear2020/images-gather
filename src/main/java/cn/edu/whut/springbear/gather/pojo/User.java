package cn.edu.whut.springbear.gather.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-10 23:58 Wednesday
 */
@Data
public class User implements Serializable {
    private static final long serialVersionUID = -4649863508706693629L;

    /**
     * User status
     */
    public static final int STATUS_NORMAL = 0;
    public static final int STATUS_ABNORMAL = 1;
    /**
     * User type
     */
    public static final int TYPE_STUDENT = 0;
    public static final int TYPE_MONITOR = 1;
    public static final int TYPE_TEACHER = 2;

    private Integer id;
    private String username;
    @JsonIgnore
    private String password;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastLoginDatetime;
    private String phone;
    private String email;
    private Integer userStatus;
    private Integer userType;

    private People people;
}
