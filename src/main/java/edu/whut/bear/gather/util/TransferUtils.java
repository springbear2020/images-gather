package edu.whut.bear.gather.util;

import edu.whut.bear.gather.pojo.Response;
import edu.whut.bear.gather.pojo.User;

import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 6/9/2022 3:56 PM
 */
public class TransferUtils {
    /**
     * 判断三张图片是否是 image/* 文件格式
     *
     * @param healthType   健康码文件格式
     * @param scheduleType 行程码文件格式
     * @param closedType   密接查文件格式
     * @return null - 全都是文件格式
     */
    public static Response isImage(String healthType, String scheduleType, String closedType) {
        // 判断三张图片的格式是否是 image
        if (!healthType.contains("image")) {
            return Response.info("请选择图片格式的健康码");
        }
        if (!scheduleType.contains("image")) {
            return Response.info("请选择图片格式的行程码");
        }
        if (!closedType.contains("image")) {
            return Response.info("请选择图片格式的密接查");
        }
        return null;
    }

    /**
     * 格式化图片的全文件名
     *
     * @param healthSuffix   健康码文件后缀
     * @param scheduleSuffix 行程码文件后缀
     * @param closedSuffix   密接查文件后缀
     * @return String[0]：健康码文件名     String[1]：行程码文件名    String[2]：密接查文件名
     */
    public static String[] imageFullFileName(User user, String healthSuffix, String scheduleSuffix, String closedSuffix) {
        String healthFullFileName = user.getClassNumber() + "-" + user.getRealName() + "-" + user.getUsername() + "-"
                + "健康码" + "-" + DateUtils.parseDateNoHyphenDate(new Date()) + healthSuffix;
        String scheduleFullFileName = user.getClassNumber() + "-" + user.getRealName() + "-" + user.getUsername() + "-"
                + "行程码" + "-" + DateUtils.parseDateNoHyphenDate(new Date()) + scheduleSuffix;
        String closedFullFileName = user.getClassNumber() + "-" + user.getRealName() + "-" + user.getUsername() + "-"
                + "密接查" + "-" + DateUtils.parseDateNoHyphenDate(new Date()) + closedSuffix;
        return new String[]{healthFullFileName, scheduleFullFileName, closedFullFileName};
    }
}
