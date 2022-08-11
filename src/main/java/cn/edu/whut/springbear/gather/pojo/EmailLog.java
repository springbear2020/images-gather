package cn.edu.whut.springbear.gather.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-11 01:35 Thursday
 */
@Data
@NoArgsConstructor
public class EmailLog implements Serializable {
    private static final long serialVersionUID = -7702847140100066460L;

    private Integer id;
    private String email;
    private String code;
    private Date datetime;
    private Integer userId;

    public EmailLog(String email, String code, Date datetime, Integer userId) {
        this.email = email;
        this.code = code;
        this.datetime = datetime;
        this.userId = userId;
    }
}