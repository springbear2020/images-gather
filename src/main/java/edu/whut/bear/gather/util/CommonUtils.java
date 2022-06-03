package edu.whut.bear.gather.util;

import edu.whut.bear.gather.pojo.Upload;

/**
 * @author Spring-_-Bear
 * @datetime 6/3/2022 12:45 AM
 */
public class CommonUtils {
    /**
     * Get the file chinese name by the file type
     *
     * @param fileType File type
     * @return File type name or null
     */
    public static String getFileType(Integer fileType) {
        String type = null;
        if (Upload.HEALTH_IMAGE == fileType) {
            type = "健康码";
        } else if (Upload.SCHEDULE_IMAGE == fileType) {
            type = "行程码";
        } else if (Upload.CLOSED_IMAGE == fileType) {
            type = "密接查";
        }
        return type;
    }
}
