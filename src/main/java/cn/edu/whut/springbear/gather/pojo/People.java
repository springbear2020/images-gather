package cn.edu.whut.springbear.gather.pojo;

import cn.edu.whut.springbear.gather.util.poi.annation.SheetColumnName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-11 15:09 Thursday
 */
@Data
@NoArgsConstructor
public class People implements Serializable {
    private static final long serialVersionUID = 8582823655075281241L;

    private Integer id;
    @SheetColumnName("学号")
    private String number;
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
    private Integer classId;
    private Integer gradeId;
    private Integer schoolId;

    @SheetColumnName("备注")
    private String comment;

    public People(Integer id, String sex, String phone, String email) {
        this.id = id;
        this.sex = sex;
        this.phone = phone;
        this.email = email;
    }
}
