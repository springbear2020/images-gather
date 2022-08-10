package cn.edu.whut.springbear.gather.util;

import org.junit.Test;


/**
 * @author Spring-_-Bear
 * @datetime 2022-08-10 16:32 Wednesday
 */
public class FileUtilsTest {

    @Test
    public void compress() {
        System.out.println(FileUtils.compress("C:\\Users\\Admin\\OneDrive\\Documents", "C:\\Users\\Admin\\Desktop\\temp.zip"));
    }
}