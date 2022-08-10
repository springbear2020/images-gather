package cn.edu.whut.springbear.gather.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-08 22:01 Monday
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginLog implements Serializable {
    private static final long serialVersionUID = -7095349991287590023L;

    private Integer id;
    private String ip;
    private String location;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date loginDatetime;
    private Integer userId;
}
