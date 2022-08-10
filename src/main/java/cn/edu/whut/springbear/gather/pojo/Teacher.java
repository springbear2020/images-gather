package cn.edu.whut.springbear.gather.pojo;

import cn.edu.whut.springbear.gather.util.poi.annation.SheetColumnName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-10 09:12 Wednesday
 */
@Data
public class Teacher implements Serializable {
    private static final long serialVersionUID = -2331807332500911607L;

    private Integer id;
    @SheetColumnName("姓名")
    private String name;
    @SheetColumnName("性别")
    private String sex;
    @SheetColumnName("电话")
    private String phone;
    @SheetColumnName("邮箱")
    private String email;
    @SheetColumnName("班级")
    private String className;
    @SheetColumnName("年级")
    private String grade;
    @SheetColumnName("学校")
    private String school;
    private Integer userId;
}
