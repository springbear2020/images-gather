package edu.whut.bear.gather.controller;

import edu.whut.bear.gather.pojo.Response;
import edu.whut.bear.gather.pojo.Upload;
import edu.whut.bear.gather.pojo.User;
import edu.whut.bear.gather.util.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author Spring-_-Bear
 * @datetime 6/6/2022 9:11 PM
 */
@RestController
public class TransferController {
    @Autowired
    private QiniuUtils qiniuUtils;

    @GetMapping("/transfer/upload/images")
    public Response getFiesUploadToken(HttpSession session) {
        User user = (User) session.getAttribute("user");
        User admin = (User) session.getAttribute("admin");

        // 管理员和用户在同一浏览器同时在线
        if (user != null && admin != null) {
            return Response.info("请先退出管理员或普通用户账号");
        }
        // 判断是管理员还是用户上传图片
        user = admin == null ? user : admin;

        assert user != null;
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
            return Response.error("请求获取上传验证信息失败");
        }

        return Response.success("成功获取三条 token 信息").put("keyList", keyList).put("tokenList", tokenList);
    }
}
