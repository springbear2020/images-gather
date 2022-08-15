package cn.edu.whut.springbear.gather.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-10 23:58 Wednesday
 */
@Data
@NoArgsConstructor
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
    public static final int TYPE_STUDENT = 1;
    public static final int TYPE_MONITOR = 2;
    public static final int TYPE_HEAD_TEACHER = 3;
    public static final int TYPE_GRADE_TEACHER = 4;
    public static final int TYPE_ADMIN = 5;

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
    private Date createDatetime;

    public User(Integer id, Date lastLoginDatetime) {
        this.id = id;
        this.lastLoginDatetime = lastLoginDatetime;
    }

    public User(Integer id, String password) {
        this.id = id;
        this.password = password;
    }

    public User(Integer id, String phone, String email) {
        this.id = id;
        this.phone = phone;
        this.email = email;
    }

    public User(String username, String password, Date lastLoginDatetime, String phone, String email, Integer userStatus, Integer userType, Date createDatetime) {
        this.username = username;
        this.password = password;
        this.lastLoginDatetime = lastLoginDatetime;
        this.phone = phone;
        this.email = email;
        this.userStatus = userStatus;
        this.userType = userType;
        this.createDatetime = createDatetime;
    }

    private People people;
}
