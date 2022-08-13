package cn.edu.whut.springbear.gather.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-13 08:51 Saturday
 */
@Data
public class Grade implements Serializable {
    private static final long serialVersionUID = 8562515620289688457L;

    private Integer id;
    private Integer grade;
}
