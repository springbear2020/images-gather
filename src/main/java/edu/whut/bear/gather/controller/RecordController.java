package edu.whut.bear.gather.controller;

import edu.whut.bear.gather.pojo.Record;
import edu.whut.bear.gather.pojo.Response;
import edu.whut.bear.gather.pojo.Upload;
import edu.whut.bear.gather.pojo.User;
import edu.whut.bear.gather.service.RecordService;
import edu.whut.bear.gather.util.DateUtils;
import edu.whut.bear.gather.util.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 6/7/2022 7:56 AM
 */
@RestController
public class RecordController {
    @Autowired
    private RecordService recordService;
    @Autowired
    private PropertyUtils propertyUtils;

    @PostMapping("/record/upload/{status}")
    public Response saveUploadUpdateRecord(@PathVariable("status") Integer status, HttpSession session,
                                           @RequestParam("healthKey") String healthKey, @RequestParam("scheduleKey") String scheduleKey, @RequestParam("closedKey") String closedKey) {
        // 非法的今日上传记录状态
        if (status != Record.NO && status != Record.YES) {
            return Response.error("图片上传成功，非法的上传记录状态");
        }

        User user = (User) session.getAttribute("user");
        User admin = (User) session.getAttribute("admin");

        // 管理员和用户在同一浏览器同时在线
        if (user != null && admin != null) {
            return Response.info("请先退出管理员或个人用户账号");
        }
        // 判断是管理员还是用户上传记录
        user = admin == null ? user : admin;

        // 批量插入三条用户上传记录 Upload 并返回自增 ID 数组
        assert user != null;
        String bucket = propertyUtils.getBucket();
        String domain = propertyUtils.getDomain();
        Upload healthUpload = new Upload(user.getId(), Upload.HEALTH_IMAGE, new Date(), bucket, domain, healthKey);
        Upload scheduleUpload = new Upload(user.getId(), Upload.SCHEDULE_IMAGE, new Date(), bucket, domain, scheduleKey);
        Upload closedUpload = new Upload(user.getId(), Upload.CLOSED_IMAGE, new Date(), bucket, domain, closedKey);
        // 保存到数据库并获得自增 ID 值，传参顺序固定不可变
        int[] autoIncrementIds = recordService.saveUploadBatch(healthUpload, scheduleUpload, closedUpload);
        if (autoIncrementIds.length != 3) {
            return Response.error("上传记录保存失败，请联系系统管理员");
        }

        // 获取用户今日记录（登入系统时创建）
        Record userRecordToday = recordService.getUserRecordByDate(user.getId(), new Date());
        if (userRecordToday == null) {
            return Response.info("不存在今日记录，请联系系统管理员");
        }

        // 更新用户今日记录 Record 中的上传记录 ID、图片访问 url 和记录状态
        userRecordToday.setStatus(status);
        userRecordToday.setHealthUploadId(autoIncrementIds[0]);
        userRecordToday.setScheduleUploadId(autoIncrementIds[1]);
        userRecordToday.setClosedUploadId(autoIncrementIds[2]);
        userRecordToday.setHealthImageUrl(domain + healthKey);
        userRecordToday.setScheduleImageUrl(domain + scheduleKey);
        userRecordToday.setClosedImageUrl(domain + closedKey);
        // 更新用户今日上传记录
        if (!recordService.updateRecordState(userRecordToday)) {
            return Response.error("今日记录更新失败，请联系系统管理员");
        }
        return Response.success("今日【两码一查】已上传");
    }

    @GetMapping("/record/user/today")
    public Response getUserRecordToday(HttpSession session) {
        User user = (User) session.getAttribute("user");
        User admin = (User) session.getAttribute("admin");

        // 管理员和用户在同一浏览器同时在线
        if (user != null && admin != null) {
            return Response.error("请先退出管理员或普通用户账号");
        }
        // 判断是管理员还是用户上传记录
        user = admin == null ? user : admin;

        // 获取用户今日记录（登入系统时创建）
        assert user != null;
        Record userRecordToday = recordService.getUserRecordByDate(user.getId(), new Date());
        if (userRecordToday == null) {
            // 如果为空，则用户尚未登入系统
            return Response.error("不存在今日记录，请联系系统管理员");
        }
        if (userRecordToday.getStatus() == Record.NO) {
            // 用户已登入系统，今日记录已存在，但尚未上传图片
            return Response.success("请选择并上传您的【两码一查】");
        }
        return Response.info("今日【两码一查】已完成").put("userRecordToday", userRecordToday);
    }


    @GetMapping("/admin/record/class/{date}")
    public Response getClassRecord(@PathVariable("date") String date, HttpSession session) {
        User admin = (User) session.getAttribute("admin");
        // 获取管理员本班的学生记录信息并处理
        assert admin != null;
        return recordService.processClassRecordList(admin.getClassNumber(), DateUtils.parseString(date));
    }

    @GetMapping("/admin/record/grade/{date}")
    public Response getGradeRecord(@PathVariable("date") String date, HttpSession session) {
        User admin = (User) session.getAttribute("admin");
        assert admin != null;
        // 获取管理员该年级的年级记录信息并处理
        return recordService.getGradeUnUploadUserList(admin.getGrade(), DateUtils.parseString(date));
    }
}
