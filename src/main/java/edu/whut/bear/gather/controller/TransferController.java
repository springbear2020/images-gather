package edu.whut.bear.gather.controller;

import edu.whut.bear.gather.pojo.Response;
import edu.whut.bear.gather.pojo.Upload;
import edu.whut.bear.gather.pojo.User;
import edu.whut.bear.gather.service.RecordService;
import edu.whut.bear.gather.service.TransferService;
import edu.whut.bear.gather.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
    @PostMapping("/transfer/upload/{fileType}")
    public Response imageUpload(@PathVariable("fileType") Integer fileType, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Response.info("登录后方可上传文件");
        }

        String fileTypeString;
        switch (fileType) {
            case Upload.HEALTH_IMAGE:
                fileTypeString = "健康码";
                break;
            case Upload.SCHEDULE_IMAGE:
                fileTypeString = "行程码";
                break;
            case Upload.CLOSED_IMAGE:
                fileTypeString = "密接查";
                break;
            default:
                fileTypeString = null;
        }
        if (fileTypeString == null) {
            // The upload type must be 1 or 2 or 3
            return Response.info("请上传正确的文件类型");
        }

        // File name like 李春雄-行程码-0121910870705.png
        String filename = user.getRealName() + "-" + fileTypeString + "-" + user.getUsername() + ".png";
        // Give a new file name of the file like 2022-06-03/软件zy1901/李春雄-行程码-0121910870705.png saved by Qiniu cloud
        String key = DateUtils.parseDate(new Date()) + "/" + user.getClassName() + "/" + filename;

        // Get Qiniu upload toke form Qiniu server -> token[0]:domain    token[1]:bucket   token[2]:uploadToken
        String[] token = transferService.getFileUploadToken(key, fileType);
        // Save the upload record to database
        Upload upload = new Upload(user.getId(), fileType, new Date(), token[1], token[0], key);
        if (!recordService.saveUpload(upload)) {
            return Response.error("文件上传记录保存失败");
        }
        // Update the file upload record


        return Response.success("Get token successfully").put("key", key).put("token", token[2]);
    }
}
