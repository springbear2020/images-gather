package cn.edu.whut.springbear.gather.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-13 08:56 Saturday
 */
@Data
public class Class implements Serializable {
    private static final long serialVersionUID = -358896718835176222L;

    private Integer id;
    private String className;

    /**
     * The three images not upload people total numbers of the class
     */
    private Integer notCompletedNums;
}
