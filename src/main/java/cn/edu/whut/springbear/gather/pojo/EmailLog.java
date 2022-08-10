package cn.edu.whut.springbear.gather.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-09 15:26 Tuesday
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailLog implements Serializable {

    private static final long serialVersionUID = -7702847140100066460L;

    private Integer id;
    private String email;
    private String code;
    private Date datetime;
}
