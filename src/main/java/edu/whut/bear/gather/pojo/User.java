package edu.whut.bear.gather.pojo;

import lombok.Data;
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
    private Date lastLoginDate;
}
