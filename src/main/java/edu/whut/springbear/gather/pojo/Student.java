package edu.whut.springbear.gather.pojo;


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
    private String number;
    private String name;
    private String sex;
    private String phone;
    private String email;
    private String major;
    private String className;
    private String grade;
    private String college;
    private String school;
    private Integer userId;
}
