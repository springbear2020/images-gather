package edu.whut.springbear.gather.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.whut.springbear.converter.annotation.ExcelColumnName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 2022-06-30 21:28 Thursday
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginLog implements Serializable {
    private static final long serialVersionUID = -7095349991287590023L;

    private Integer id;
    @ExcelColumnName("IP")
    private String ip;
    @ExcelColumnName("location")
    private String location;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelColumnName("datetime")
    private Date loginDateTime;
    @ExcelColumnName("userId")
    private Integer userId;
}
