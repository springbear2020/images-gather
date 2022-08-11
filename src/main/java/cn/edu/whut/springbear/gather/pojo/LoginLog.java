package cn.edu.whut.springbear.gather.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-11 00:07 Thursday
 */
@Data
@NoArgsConstructor
public class LoginLog implements Serializable {
    private static final long serialVersionUID = -7095349991287590023L;

    private Integer id;
    private String ip;
    private String location;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date loginDatetime;
    private Integer userId;

    public LoginLog(String ip, String location, Date loginDatetime, Integer userId) {
        this.ip = ip;
        this.location = location;
        this.loginDatetime = loginDatetime;
        this.userId = userId;
    }
}
