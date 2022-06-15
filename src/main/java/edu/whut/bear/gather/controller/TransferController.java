package edu.whut.bear.gather.controller;

import edu.whut.bear.gather.pojo.Record;
import edu.whut.bear.gather.pojo.Response;
import edu.whut.bear.gather.pojo.Upload;
import edu.whut.bear.gather.pojo.User;
import edu.whut.bear.gather.service.RecordService;
import edu.whut.bear.gather.util.DateUtils;
import edu.whut.bear.gather.util.PropertyUtils;
import edu.whut.bear.gather.util.QiniuUtils;
import edu.whut.bear.gather.util.TransferUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author Spring-_-Bear
 * @datetime 6/6/2022 9:11 PM
 */
@RestController
@SuppressWarnings("all")
public class TransferController {
    @Autowired
    private QiniuUtils qiniuUtils;
    @Autowired
    private PropertyUtils propertyUtils;
    @Autowired
    private RecordService recordService;

    @GetMapping("/transfer/qiniu/upload/images")
    public Response getFiesUploadToken(HttpSession session) {
        User user = (User) session.getAttribute("user");
        User admin = (User) session.getAttribute("admin");

        // 管理员和用户在同一浏览器同时在线
        if (user != null && admin != null) {
            return Response.info("请先退出管理员或普通用户账号");
        }
        // 判断是管理员还是用户上传图片
        user = admin == null ? user : admin;

        // 重命名文件 2022-06-03/1-李春雄-0121910870705-健康码-20220223.png
        String healthImageFileName = qiniuUtils.getImageFormatFileName(user, Upload.HEALTH_IMAGE);
        // 重命名文件 2022-06-03/1-李春雄-0121910870705-行程码-20220223.png
        String scheduleImageFileName = qiniuUtils.getImageFormatFileName(user, Upload.SCHEDULE_IMAGE);
        // 重命名文件 2022-06-03/1-李春雄-0121910870705-密接查-20220223.png
        String closedImageFileName = qiniuUtils.getImageFormatFileName(user, Upload.CLOSED_IMAGE);

        // 返回给客户端的文件名、token 验证信息列表
        List<String> keyList = new ArrayList<>();
        keyList.add(healthImageFileName);
        keyList.add(scheduleImageFileName);
        keyList.add(closedImageFileName);

        // 从七牛云平台获取三条文件上传 token 验证信息
        List<String> tokenList = new ArrayList<>();
        tokenList.add(qiniuUtils.getImageUploadToken(healthImageFileName));
        tokenList.add(qiniuUtils.getImageUploadToken(scheduleImageFileName));
        tokenList.add(qiniuUtils.getImageUploadToken(closedImageFileName));

        if (tokenList.size() <= 0) {
            return Response.error("获取 token 失败，请联系系统管理员");
        }

        return Response.success("成功获取 token 信息").put("keyList", keyList).put("tokenList", tokenList);
    }

    @PostMapping("/transfer/local/upload/images")
    public Response filesUpload(HttpSession session,
                                @RequestParam("healthImage") MultipartFile healthImage,
                                @RequestParam("scheduleImage") MultipartFile scheduleImage,
                                @RequestParam("closedImage") MultipartFile closedImage) {
        if (healthImage == null || scheduleImage == null || closedImage == null) {
            return Response.info("请正确选择【两码一查】图片");
        }

        // 判断三种图片是否均是 image/* 格式的图片文件
        Response response = TransferUtils.isImage(Objects.requireNonNull(healthImage.getContentType()), Objects.requireNonNull(scheduleImage.getContentType()), Objects.requireNonNull(closedImage.getContentType()));
        if (response != null) {
            return response;
        }

        // 判断管理员和用户在同一浏览器同时在线
        User user = (User) session.getAttribute("user");
        User admin = (User) session.getAttribute("admin");
        if (user != null && admin != null) {
            return Response.info("请先退出管理员或普通用户账号");
        }
        // 判断是管理员还是用户上传图片
        user = admin == null ? user : admin;

        // 创建班级今日文件保存目录
        String realPath = session.getServletContext().getRealPath("/static/gather");
        String todayDate = DateUtils.parseDate(new Date());
        File classTodayDirectory = new File(realPath + "/" + user.getClassName() + "/" + todayDate);
        if (!classTodayDirectory.exists() && !classTodayDirectory.mkdirs()) {
            return Response.error("班级今日目录创建失败，请联系系统管理员");
        }

        // 获取三张图片的原文件名以获取文件后缀
        String healthFilename = healthImage.getOriginalFilename();
        String scheduleFilename = scheduleImage.getOriginalFilename();
        String closedFilename = closedImage.getOriginalFilename();
        // 源文件后缀
        String healthSuffix = healthFilename.substring(healthFilename.lastIndexOf('.'));
        String scheduleSuffix = scheduleFilename.substring(scheduleFilename.lastIndexOf('.'));
        String closedSuffix = closedFilename.substring(closedFilename.lastIndexOf('.'));
        // 获取用户今日图片文件命名
        String[] fileNames = TransferUtils.imageFullFileName(user, healthSuffix, scheduleSuffix, closedSuffix);

        try {
            // 健康码文件写入磁盘
            healthImage.transferTo(new File(classTodayDirectory + "/" + fileNames[0]));
        } catch (IOException e) {
            return Response.error("健康码写入磁盘失败，请联系系统管理员");
        }

        try {
            // 行程码文件写入磁盘
            scheduleImage.transferTo(new File(classTodayDirectory + "/" + fileNames[1]));
        } catch (IOException e) {
            return Response.error("行程码写入磁盘失败，请联系系统管理员");
        }

        try {
            // 健康码文件写入磁盘
            closedImage.transferTo(new File(classTodayDirectory + "/" + fileNames[2]));
        } catch (IOException e) {
            return Response.error("密接查写入磁盘失败，请联系系统管理员");
        }

        // 更新用户今日记录（图片访问 url 及记录状态）
        String healthImageUrl = propertyUtils.getContextPath() + "static/gather/" + user.getClassName() + "/" + todayDate + "/" + fileNames[0];
        String scheduleImageUrl = propertyUtils.getContextPath() + "static/gather/" + user.getClassName() + "/" + todayDate + "/" + fileNames[1];
        String closedImageUrl = propertyUtils.getContextPath() + "static/gather/" + user.getClassName() + "/" + todayDate + "/" + fileNames[2];

        // 获取用户今日记录（登入系统时创建）
        Record userRecordToday = recordService.getUserRecordByDate(user.getId(), new Date());
        if (userRecordToday == null) {
            return Response.info("不存在今日记录，请联系系统管理员");
        }

        // 更新用户今日记录 Record 中的上传记录 ID、图片访问 url 和记录状态
        userRecordToday.setStatus(Record.YES);
        userRecordToday.setHealthUploadId(Record.DEFAULT_RECORD_ID);
        userRecordToday.setScheduleUploadId(Record.DEFAULT_RECORD_ID);
        userRecordToday.setClosedUploadId(Record.DEFAULT_RECORD_ID);
        userRecordToday.setHealthImageUrl(healthImageUrl);
        userRecordToday.setScheduleImageUrl(scheduleImageUrl);
        userRecordToday.setClosedImageUrl(closedImageUrl);
        // 更新用户今日上传记录
        if (!recordService.updateRecordState(userRecordToday)) {
            return Response.error("今日记录更新失败，请联系系统管理员");
        }
        return Response.success("今日【两码一查】已上传");
    }
}
