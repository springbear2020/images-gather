package cn.edu.whut.springbear.gather.pojo;

import cn.edu.whut.springbear.gather.util.poi.annation.SheetColumnName;
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
    public static final int TYPE_STUDENT = 1;
    public static final int TYPE_MONITOR = 2;
    public static final int TYPE_HEAD_TEACHER = 3;
    public static final int TYPE_GRADE_TEACHER = 4;
    public static final int TYPE_ADMIN = 5;

    private Integer id;
    @SheetColumnName("学号")
    private String username;
    @JsonIgnore
    private String password;
    @SheetColumnName("电话")
    private String phone;
    @SheetColumnName("邮箱")
    private String email;
    @SheetColumnName("姓名")
    private String name;
    @SheetColumnName("性别")
    private String sex;
    private Integer schoolId;
    private Integer gradeId;
    private Integer classId;
    private Integer userType;
    private Integer userStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastLoginDatetime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDatetime;

    @SheetColumnName("学校")
    private String school;
    @SheetColumnName("年级")
    private String grade;
    @SheetColumnName("班级")
    private String className;
    @SheetColumnName("备注")
    private String comment;
}
