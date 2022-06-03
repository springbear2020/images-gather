package edu.whut.bear.gather.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 6/2/2022 11:42 PM
 */
@Data
@Component
public class Login {
    private Integer id;
    private Integer userId;
    private String ip;
    private String location;
    private Date loginTime;

    public Login() {
    }

    public Login(Integer id, Integer userId, String ip, String location, Date loginTime) {
        this.id = id;
        this.userId = userId;
        this.ip = ip;
        this.location = location;
        this.loginTime = loginTime;
    }
}
