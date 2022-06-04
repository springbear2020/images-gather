package edu.whut.bear.gather.controller;

import edu.whut.bear.gather.pojo.Record;
import edu.whut.bear.gather.pojo.Response;
import edu.whut.bear.gather.pojo.Upload;
import edu.whut.bear.gather.pojo.User;
import edu.whut.bear.gather.service.RecordService;
import edu.whut.bear.gather.service.TransferService;
import edu.whut.bear.gather.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * @author Spring-_-Bear
 * @datetime 6/3/2022 12:09 AM
 */
@Controller
public class TransferController {
    @Autowired
    private TransferService transferService;
    @Autowired
    private RecordService recordService;

    @ResponseBody
    @PostMapping("/transfer/upload/health")
    public Response healthImageUpload(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Response.info("登录后方可上传健康码图片文件");
        }

        // Give a new file name like 2022-06-03/软件zy1901/李春雄-健康码-0121910870705.png
        String key = DateUtils.parseDate(new Date()) + "/" + user.getClassName() + "/" + user.getRealName() + "-" + "健康码" + "-" + user.getUsername() + ".png";

        // token[0]:domain    token[1]:bucket   token[2]:uploadToken
        String[] token = transferService.getFileUploadToken(key);

        // Save the upload record
        Upload upload = new Upload(user.getId(), Upload.HEALTH_IMAGE, new Date(), token[1], token[0], key);
        if (!recordService.saveUpload(upload)) {
            return Response.error("健康码上传记录保存失败");
        }

        // Add the health upload info to the record
        Record record = new Record();
        record.setHealthUploadId(upload.getId());
        record.setHealthImageUrl(upload.getDomain() + upload.getKey());
        session.setAttribute("record", record);

        return Response.success("健康码文件上传成功").put("key", key).put("token", token[2]);
    }

    @ResponseBody
    @PostMapping("/transfer/upload/schedule")
    public Response scheduleImageUpload(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Response.info("登录后方可上传行程卡图片文件");
        }

        // Give a new file name like 2022-06-03/软件zy1901/李春雄-行程卡-0121910870705.png
        String key = DateUtils.parseDate(new Date()) + "/" + user.getClassName() + "/" + user.getRealName() + "-" + "行程卡" + "-" + user.getUsername() + ".png";

        // token[0]:domain    token[1]:bucket   token[2]:uploadToken
        String[] token = transferService.getFileUploadToken(key);

        // Save the upload record
        Upload upload = new Upload(user.getId(), Upload.SCHEDULE_IMAGE, new Date(), token[1], token[0], key);
        if (!recordService.saveUpload(upload)) {
            return Response.error("行程卡上传记录保存失败");
        }

        // Add the schedule upload info to the record
        Record record = (Record) session.getAttribute("record");
        record.setScheduleUploadId(upload.getId());
        record.setScheduleImageUrl(upload.getDomain() + upload.getKey());
        session.setAttribute("record", record);

        return Response.success("行程卡文件上传成功").put("key", key).put("token", token[2]);
    }

    @ResponseBody
    @PostMapping("/transfer/upload/closed")
    public Response closedImageUpload(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Response.info("登录后方可上传密接查图片文件");
        }

        // Give a new file name like 2022-06-03/软件zy1901/李春雄-密接查-0121910870705.png
        String key = DateUtils.parseDate(new Date()) + "/" + user.getClassName() + "/" + user.getRealName() + "-" + "密接查" + "-" + user.getUsername() + ".png";

        // token[0]:domain    token[1]:bucket   token[2]:uploadToken
        String[] token = transferService.getFileUploadToken(key);

        // Save the upload record
        Upload upload = new Upload(user.getId(), Upload.CLOSED_IMAGE, new Date(), token[1], token[0], key);
        if (!recordService.saveUpload(upload)) {
            return Response.error("密接查上传记录保存失败");
        }

        // Get the user's record of today
        Record userRecordToday = recordService.getUserRecordToday(user.getId(), new Date());
        // Add the schedule upload info to the record
        Record record = (Record) session.getAttribute("record");
        record.setClosedUploadId(upload.getId());
        record.setClosedImageUrl(upload.getDomain() + upload.getKey());
        record.setId(userRecordToday.getId());
        // Update the healthUploadId,healthImageUrl,scheduleUploadId,scheduleImageUrl,closedUploadId,closedImageUrl
        if (!recordService.updateRecordState(record)) {
            return Response.info("【两码一查】文件上传成功，待管理员审核");
        }
        return Response.success("今日【两码一查】已完成").put("key", key).put("token", token[2]);
    }
}
