package edu.whut.bear.gather.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;

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
    private String className;
}
