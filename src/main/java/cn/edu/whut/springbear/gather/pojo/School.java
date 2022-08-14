package cn.edu.whut.springbear.gather.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-14 08:32 Sunday
 */
@Data
@NoArgsConstructor
public class School implements Serializable {
    private static final long serialVersionUID = 4472985043693820422L;

    private Integer id;
    private String school;

    public School(String school) {
        this.school = school;
    }
}
