package cn.edu.whut.springbear.gather.util;

import java.io.File;

/**
 * @author Spring-_-Bear
 * @datetime 2022-08-11 16:18 Thursday
 */
public class FileUtils {
    /**
     * Determine whether the directory of the specified path exists, create it if it does not exists
     */
    public static boolean createDirectory(String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            return directory.mkdirs();
        }
        return true;
    }
}
