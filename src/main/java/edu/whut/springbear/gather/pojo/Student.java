package edu.whut.springbear.gather.pojo;


import edu.whut.springbear.converter.annotation.ExcelColumnName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Spring-_-Bear
 * @datetime 2022-06-30 15:24 Thursday
 */
@Data
public class Student implements Serializable {
    private static final long serialVersionUID = 213698552100840584L;

    private Integer id;
    @ExcelColumnName("学号")
    private String number;
    @ExcelColumnName("姓名")
    private String name;
    @ExcelColumnName("性别")
    private String sex;
    @ExcelColumnName("电话")
    private String phone;
    @ExcelColumnName("邮箱")
    private String email;
    @ExcelColumnName("专业")
    private String major;
    @ExcelColumnName("班级")
    private String className;
    @ExcelColumnName("年级")
    private String grade;
    @ExcelColumnName("学院")
    private String college;
    @ExcelColumnName("学校")
    private String school;
    private Integer userId;
}
