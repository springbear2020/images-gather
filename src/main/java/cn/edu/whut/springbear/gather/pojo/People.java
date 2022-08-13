package cn.edu.whut.springbear.gather.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-11 15:09 Thursday
 */
@Data
public class People implements Serializable {
    private static final long serialVersionUID = 8582823655075281241L;

    private Integer id;
    private String number;
    private String name;
    private String sex;
    private String phone;
    private String email;
    private String className;
    private String grade;
    private String school;
    private Integer userId;
    private Integer classId;
    private Integer gradeId;
    private Integer schoolId;
}
